/*
  Part of the Shapes 3D library for Processing 
  	http://www.lagers.org.uk

  Copyright (c) 2010 Peter Lager

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

/*
 * The algorithms used in this class are based on the Rotation class 
 * found in the Apache Commons Math project at http://commons.apache.org/math/ <br>
 * 
 * It has been rewritten to make use of the PVector class that is 
 * part of Processing.
 */

package rosprocessing;

import java.io.Serializable;

import processing.core.PVector;

/**
 * This class implements rotations in a three-dimensional space.
 *
 * <p>Rotations can be represented by several different mathematical
 * entities (matrices, axe and angle, Cardan or Euler angles,
 * quaternions). This class presents an higher level abstraction, more
 * user-oriented and hiding this implementation details. Well, for the
 * curious, we use quaternions for the internal representation. The
 * user can build a rotation from any of these representations, and
 * any of these representations can be retrieved from a
 * <code>Rotation</code> instance (see the various constructors and
 * getters). In addition, a rotation can also be built implicitly
 * from a set of vectors and their image.</p>
 * <p>This implies that this class can be used to convert from one
 * representation to another one. For example, converting a rotation
 * matrix into a set of Cardan angles from can be done using the
 * following single line of code:</p>
 * <pre>
 * float[] angles = new Rotation(matrix, 1.0fe-10).getAngles(RotationOrder.XYZ);
 * </pre>
 * <p>Focus is oriented on what a rotation <em>do</em> rather than on its
 * underlying representation. Once it has been built, and regardless of its
 * internal representation, a rotation is an <em>operator</em> which basically
 * transforms three dimensional {@link PVector vectors} into other three
 * dimensional {@link PVector vectors}. Depending on the application, the
 * meaning of these vectors may vary and the semantics of the rotation also.</p>
 * <p>For example in an spacecraft attitude simulation tool, users will often
 * consider the vectors are fixed (say the Earth direction for example) and the
 * rotation transforms the coordinates coordinates of this vector in inertial
 * frame into the coordinates of the same vector in satellite frame. In this
 * case, the rotation implicitly defines the relation between the two frames.
 * Another example could be a telescope control application, where the rotation
 * would transform the sighting direction at rest into the desired observing
 * direction when the telescope is pointed towards an object of interest. In this
 * case the rotation transforms the directionf at rest in a topocentric frame
 * into the sighting direction in the same topocentric frame. In many case, both
 * approaches will be combined, in our telescope example, we will probably also
 * need to transform the observing direction in the topocentric frame into the
 * observing direction in inertial frame taking into account the observatory
 * location and the Earth rotation.</p>
 *
 * <p>These examples show that a rotation is what the user wants it to be, so this
 * class does not push the user towards one specific definition and hence does not
 * provide methods like <code>projectVectorIntoDestinationFrame</code> or
 * <code>computeTransformedDirection</code>. It provides simpler and more generic
 * methods: {@link #applyTo(PVector) applyTo(PVector)} and {@link
 * #applyInverseTo(PVector) applyInverseTo(PVector)}.</p>
 *
 * <p>Since a rotation is basically a vectorial operator, several rotations can be
 * composed together and the composite operation <code>r = r<sub>1</sub> o
 * r<sub>2</sub></code> (which means that for each vector <code>u</code>,
 * <code>r(u) = r<sub>1</sub>(r<sub>2</sub>(u))</code>) is also a rotation. Hence
 * we can consider that in addition to vectors, a rotation can be applied to other
 * rotations as well (or to itself). With our previous notations, we would say we
 * can apply <code>r<sub>1</sub></code> to <code>r<sub>2</sub></code> and the result
 * we get is <code>r = r<sub>1</sub> o r<sub>2</sub></code>. For this purpose, the
 * class provides the methods: {@link #applyTo(PVector) applyTo(PVector)} and
 * {@link #applyInverseTo(PVector) applyInverseTo(PVector)}.</p>
 *
 * <p>Rotations are guaranteed to be immutable objects.</p>
 *
 * @version $Revision: 772119 $ $Date: 2009-05-06 05:43:28 -0400 (Wed, 06 May 2009) $
 * 
 * @see PVector
 * @see RotOrder
 */
public class Rot implements VectorConstants, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1077272288787175558L;

	/** Identity rotation. */
	public static final Rot IDENTITY = new Rot(1.0f, 0.0f, 0.0f, 0.0f, false);

	/** Scalar coordinate of the quaternion. */
	private final float q0;

	/** First coordinate of the vectorial part of the quaternion. */
	private final float q1;

	/** Second coordinate of the vectorial part of the quaternion. */
	private final float q2;

	/** Third coordinate of the vectorial part of the quaternion. */
	private final float q3;

	/** Build a rotation from the quaternion coordinates.
	 * <p>A rotation can be built from a <em>normalized</em> quaternion,
	 * i.e. a quaternion for which q<sub>0</sub><sup>2</sup> +
	 * q<sub>1</sub><sup>2</sup> + q<sub>2</sub><sup>2</sup> +
	 * q<sub>3</sub><sup>2</sup> = 1. If the quaternion is not normalized,
	 * the constructor can normalize it in a preprocessing step.</p>
	 * @param q0 scalar part of the quaternion
	 * @param q1 first coordinate of the vectorial part of the quaternion
	 * @param q2 second coordinate of the vectorial part of the quaternion
	 * @param q3 third coordinate of the vectorial part of the quaternion
	 * @param needsNormalization if true, the coordinates are considered
	 * not to be normalized, a normalization preprocessing step is performed
	 * before using them
	 */
	public Rot(float q0, float q1, float q2, float q3,
			boolean needsNormalization) {

		if (needsNormalization) {
			float inv = 1.0f / (float)Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
			q0 *= inv;
			q1 *= inv;
			q2 *= inv;
			q3 *= inv;
		}
		this.q0 = q0;
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
	}

	/** Build a rotation from an axis and an angle.
	 * <p>We use the convention that angles are oriented according to
	 * the effect of the rotation on vectors around the axis. That means
	 * that if (i, j, k) is a direct frame and if we first provide +k as
	 * the axis and PI/2 as the angle to this constructor, and then
	 * {@link #applyTo(PVector) apply} the instance to +i, we will get
	 * +j.</p>
	 * @param axis axis around which to rotate
	 * @param angle rotation angle.
	 */
	public Rot(PVector axis, float angle) {

		float norm = axis.mag();
		if (norm == 0) {
			q0 = 1;
			q1 = q2 = q3 = 0;
			//System.out.println(Messages.build("The axis vector {0} has no magnitude!!", axis));
			return;
		}
		float halfAngle = -0.5f * angle;
		float coeff = (float) Math.sin(halfAngle) / norm;

		q0 = (float) Math.cos (halfAngle);
		q1 = coeff * axis.x;
		q2 = coeff * axis.y;
		q3 = coeff * axis.z;

	}

	/** Build a rotation from a 3X3 matrix.
	 * 
	 * <p>Rotation matrices are orthogonal matrices, i.e. unit matrices
	 * (which are matrices for which m.m<sup>T</sup> = I) with real
	 * coefficients. The module of the determinant of unit matrices is
	 * 1, among the orthogonal 3X3 matrices, only the ones having a
	 * positive determinant (+1) are rotation matrices.</p>
	 * 
	 * <p>When a rotation is defined by a matrix with truncated values
	 * (typically when it is extracted from a technical sheet where only
	 * four to five significant digits are available), the matrix is not
	 * orthogonal anymore. This constructor handles this case
	 * transparently by using a copy of the given matrix and applying a
	 * correction to the copy in order to perfect its orthogonality. If
	 * the Frobenius norm of the correction needed is above the given
	 * threshold, then the matrix is considered to be too far from a
	 * true rotation matrix and an exception is thrown.<p>
	 * 
	 * @param m rotation matrix
	 * @param threshold convergence threshold for the iterative
	 * orthogonality correction (convergence is reached when the
	 * difference between two steps of the Frobenius norm of the
	 * correction is below this threshold)
	 * 
	 * @exception NotARotationMatrixException if the matrix is not a 3X3
	 * matrix, or if it cannot be transformed into an orthogonal matrix
	 * with the given threshold, or if the determinant of the resulting
	 * orthogonal matrix is negative
	 */
	public Rot(float[][] m, float threshold){
		// dimension check
		if ((m.length != 3) || (m[0].length != 3) ||
				(m[1].length != 3) || (m[2].length != 3)) {
			q0 = 1;
			q1 = q2 = q3 = 0;
			//System.out.println(Messages.build("a {0}x{1} matrix cannot be a rotation matrix", m.length, m[0].length));
			return;
		}

		// compute a "close" orthogonal matrix
		float[][] ort = orthogonalizeMatrix(m, threshold);
		if(ort == null){
			q0 = 1;
			q1 = q2 = q3 = 0;
			//System.out.println(Messages.build("unable to orthogonalize matrix in {0} iterations",10));
			return;
		}
			
		
		// check the sign of the determinant
		float det = ort[0][0] * (ort[1][1] * ort[2][2] - ort[2][1] * ort[1][2]) -
		ort[1][0] * (ort[0][1] * ort[2][2] - ort[2][1] * ort[0][2]) +
		ort[2][0] * (ort[0][1] * ort[1][2] - ort[1][1] * ort[0][2]);
		if (det < 0.0f) {
			q0 = 1;
			q1 = q2 = q3 = 0;
			//System.out.println(Messages.build("the closest orthogonal matrix has a negative determinant {0}", det));
			return;
		}

		// There are different ways to compute the quaternions elements
		// from the matrix. They all involve computing one element from
		// the diagonal of the matrix, and computing the three other ones
		// using a formula involving a division by the first element,
		// which unfortunately can be zero. Since the norm of the
		// quaternion is 1, we know at least one element has an absolute
		// value greater or equal to 0.5, so it is always possible to
		// select the right formula and avoid division by zero and even
		// numerical inaccuracy. Checking the elements in turn and using
		// the first one greater than 0.45 is safe (this leads to a simple
		// test since qi = 0.45 implies 4 qi^2 - 1 = -0.19)
		float s = ort[0][0] + ort[1][1] + ort[2][2];
		if (s > -0.19) {
			// compute q0 and deduce q1, q2 and q3
			q0 = 0.5f * (float) Math.sqrt(s + 1.0f);
			float inv = 0.25f / q0;
			q1 = inv * (ort[1][2] - ort[2][1]);
			q2 = inv * (ort[2][0] - ort[0][2]);
			q3 = inv * (ort[0][1] - ort[1][0]);
		} else {
			s = ort[0][0] - ort[1][1] - ort[2][2];
			if (s > -0.19) {
				// compute q1 and deduce q0, q2 and q3
				q1 = 0.5f * (float) Math.sqrt(s + 1.0f);
				float inv = 0.25f / q1;
				q0 = inv * (ort[1][2] - ort[2][1]);
				q2 = inv * (ort[0][1] + ort[1][0]);
				q3 = inv * (ort[0][2] + ort[2][0]);
			} else {
				s = ort[1][1] - ort[0][0] - ort[2][2];
				if (s > -0.19) {
					// compute q2 and deduce q0, q1 and q3
					q2 = 0.5f * (float) Math.sqrt(s + 1.0f);
					float inv = 0.25f / q2;
					q0 = inv * (ort[2][0] - ort[0][2]);
					q1 = inv * (ort[0][1] + ort[1][0]);
					q3 = inv * (ort[2][1] + ort[1][2]);
				} else {
					// compute q3 and deduce q0, q1 and q2
					s = ort[2][2] - ort[0][0] - ort[1][1];
					q3 = 0.5f * (float) Math.sqrt(s + 1.0f);
					float inv = 0.25f / q3;
					q0 = inv * (ort[0][1] - ort[1][0]);
					q1 = inv * (ort[0][2] + ort[2][0]);
					q2 = inv * (ort[2][1] + ort[1][2]);
				}
			}
		}
	}

	/** Build the rotation that transforms a pair of vector into another pair.
	 * 
	 * <p>Except for possible scale factors, if the instance were applied to
	 * the pair (u<sub>1</sub>, u<sub>2</sub>) it will produce the pair
	 * (v<sub>1</sub>, v<sub>2</sub>).</p>
	 * 
	 * <p>If the angular separation between u<sub>1</sub> and u<sub>2</sub> is
	 * not the same as the angular separation between v<sub>1</sub> and
	 * v<sub>2</sub>, then a corrected v'<sub>2</sub> will be used rather than
	 * v<sub>2</sub>, the corrected vector will be in the (v<sub>1</sub>,
	 * v<sub>2</sub>) plane.</p>
	 * 
	 * @param u1 first vector of the origin pair
	 * @param u2 second vector of the origin pair
	 * @param v1 desired image of u1 by the rotation
	 * @param v2 desired image of u2 by the rotation
	 * @exception IllegalArgumentException if the norm of one of the vectors is zero
	 */
	public Rot(PVector u1, PVector u2, PVector v1, PVector v2) {

		// norms computation
		float u1u1 = PVector.dot(u1, u1);
		float u2u2 = PVector.dot(u2, u2);
		float v1v1 = PVector.dot(v1, v1);
		float v2v2 = PVector.dot(v2, v2);
		if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
			q0 = 1;
			q1 = q2 = q3 = 0;
			System.out.println("zero norm for rotation defining vector");
			return;
		}

		float u1x = u1.x;
		float u1y = u1.y;
		float u1z = u1.z;

		float u2x = u2.x;
		float u2y = u2.y;
		float u2z = u2.z;

		// normalize v1 in order to have (v1'|v1') = (u1|u1)
		float coeff = (float)Math.sqrt (u1u1 / v1v1);
		float v1x   = coeff * v1.x;
		float v1y   = coeff * v1.y;
		float v1z   = coeff * v1.z;
		v1 = new PVector(v1x, v1y, v1z);

		// adjust v2 in order to have (u1|u2) = (v1|v2) and (v2'|v2') = (u2|u2)
		float u1u2   = PVector.dot(u1, u2);
		float v1v2   = PVector.dot(v1, v2);
		float coeffU = u1u2 / u1u1;
		float coeffV = v1v2 / u1u1;
		float beta   = (float) Math.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
		float alpha  = coeffU - beta * coeffV;
		float v2x    = alpha * v1x + beta * v2.x;
		float v2y    = alpha * v1y + beta * v2.y;
		float v2z    = alpha * v1z + beta * v2.z;
		v2 = new PVector(v2x, v2y, v2z);

		// preliminary computation (we use explicit formulation instead
		// of relying on the Vector3D class in order to avoid building lots
		// of temporary objects)
		PVector uRef = u1;
		PVector vRef = v1;
		float dx1 = v1x - u1.x;
		float dy1 = v1y - u1.y;
		float dz1 = v1z - u1.z;
		float dx2 = v2x - u2.x;
		float dy2 = v2y - u2.y;
		float dz2 = v2z - u2.z;
		PVector k = new PVector(dy1 * dz2 - dz1 * dy2,
				dz1 * dx2 - dx1 * dz2,
				dx1 * dy2 - dy1 * dx2);
		float c = k.x * (u1y * u2z - u1z * u2y) +
		k.y * (u1z * u2x - u1x * u2z) +
		k.z * (u1x * u2y - u1y * u2x);

		if (c == 0) {
			// the (q1, q2, q3) vector is in the (u1, u2) plane
			// we try other vectors
			PVector u3 = PVector.cross(u1, u2, null);
			PVector v3 = PVector.cross(v1, v2, null);
			float u3x  = u3.x;
			float u3y  = u3.y;
			float u3z  = u3.z;
			float v3x  = v3.x;
			float v3y  = v3.y;
			float v3z  = v3.z;

			float dx3 = v3x - u3x;
			float dy3 = v3y - u3y;
			float dz3 = v3z - u3z;
			k = new PVector(dy1 * dz3 - dz1 * dy3,
					dz1 * dx3 - dx1 * dz3,
					dx1 * dy3 - dy1 * dx3);
			c = k.x * (u1y * u3z - u1z * u3y) +
			k.y * (u1z * u3x - u1x * u3z) +
			k.z * (u1x * u3y - u1y * u3x);

			if (c == 0) {
				// the (q1, q2, q3) vector is aligned with u1:
				// we try (u2, u3) and (v2, v3)
				k = new PVector(dy2 * dz3 - dz2 * dy3,
						dz2 * dx3 - dx2 * dz3,
						dx2 * dy3 - dy2 * dx3);
				c = k.x * (u2y * u3z - u2z * u3y) +
				k.y * (u2z * u3x - u2x * u3z) +
				k.z * (u2x * u3y - u2y * u3x);

				if (c == 0) {
					// the (q1, q2, q3) vector is aligned with everything
					// this is really the identity rotation
					q0 = 1.0f;
					q1 = 0.0f;
					q2 = 0.0f;
					q3 = 0.0f;
					return;
				}

				// we will have to use u2 and v2 to compute the scalar part
				uRef = u2;
				vRef = v2;
			}
		}

		// compute the vectorial part
		c = (float) Math.sqrt(c);
		float inv = 1.0f / (c + c);
		q1 = inv * k.x;
		q2 = inv * k.y;
		q3 = inv * k.z;

		// compute the scalar part
		k = new PVector(uRef.y * q3 - uRef.z * q2,
				uRef.z * q1 - uRef.x * q3,
				uRef.x * q2 - uRef.y * q1);
		c = PVector.dot(k, k);
		q0 = PVector.dot(vRef, k) / (c + c);
	}

	/** Build one of the rotations that transform one vector into another one.
	 * 
	 * <p>Except for a possible scale factor, if the instance were
	 * applied to the vector u it will produce the vector v. There is an
	 * infinite number of such rotations, this constructor choose the
	 * one with the smallest associated angle (i.e. the one whose axis
	 * is orthogonal to the (u, v) plane). If u and v are colinear, an
	 * arbitrary rotation axis is chosen.</p>
	 * 
	 * @param u origin vector
	 * @param v desired image of u by the rotation
	 * @exception IllegalArgumentException if the norm of one of the vectors is zero
	 */
	public Rot(PVector u, PVector v) {

		float normProduct = u.mag() * v.mag();
		if (normProduct == 0) {
			q0 = 1;
			q1 = q2 = q3 = 0;
			System.out.println("zero norm for rotation defining vector");
			return;
		}

		float dot = PVector.dot(u, v);

		if (dot < ((2.0e-15 - 1.0f) * normProduct)) {
			// special case u = -v: we select a PI angle rotation around
			// an arbitrary vector orthogonal to u
			PVector w = VectorUtil.orthogonal(u);
			q0 = 0.0f;
			q1 = -w.x;
			q2 = -w.y;
			q3 = -w.z;
		} else {
			// general case: (u, v) defines a plane, we select
			// the shortest possible rotation: axis orthogonal to this plane
			q0 = (float) Math.sqrt(0.5 * (1.0f + dot / normProduct));
			float coeff = 1.0f / (2.0f * q0 * normProduct);
			q1 = coeff * (v.y * u.z - v.z * u.y);
			q2 = coeff * (v.z * u.x - v.x * u.z);
			q3 = coeff * (v.x * u.y - v.y * u.x);
		}

	}

	/** Build a rotation from three Cardan or Euler elementary rotations.
	 * 
	 * <p>Cardan rotations are three successive rotations around the
	 * canonical axes X, Y and Z, each axis being used once. There are
	 * 6 such sets of rotations (XYZ, XZY, YXZ, YZX, ZXY and ZYX). Euler
	 * rotations are three successive rotations around the canonical
	 * axes X, Y and Z, the first and last rotations being around the
	 * same axis. There are 6 such sets of rotations (XYX, XZX, YXY,
	 * YZY, ZXZ and ZYZ), the most popular one being ZXZ.</p>
	 * <p>Beware that many people routinely use the term Euler angles even
	 * for what really are Cardan angles (this confusion is especially
	 * widespread in the aerospace business where Roll, Pitch and Yaw angles
	 * are often wrongly tagged as Euler angles).</p>
	 * 
	 * @param order order of rotations to use
	 * @param alpha1 angle of the first elementary rotation
	 * @param alpha2 angle of the second elementary rotation
	 * @param alpha3 angle of the third elementary rotation
	 */
	public Rot(RotOrder order, float alpha1, float alpha2, float alpha3) {
		Rot r1 = new Rot(order.getA1(), alpha1);
		Rot r2 = new Rot(order.getA2(), alpha2);
		Rot r3 = new Rot(order.getA3(), alpha3);
		Rot composed = r1.applyTo(r2.applyTo(r3));
		q0 = composed.q0;
		q1 = composed.q1;
		q2 = composed.q2;
		q3 = composed.q3;
	}

	/** Revert a rotation.
	 * Build a rotation which reverse the effect of another
	 * rotation. This means that if r(u) = v, then r.revert(v) = u. The
	 * instance is not changed.
	 * @return a new rotation whose effect is the reverse of the effect
	 * of the instance
	 */
	public Rot revert() {
		return new Rot(-q0, q1, q2, q3, false);
	}

	/** Get the scalar coordinate of the quaternion.
	 * @return scalar coordinate of the quaternion
	 */
	public float getQ0() {
		return q0;
	}

	/** Get the first coordinate of the vectorial part of the quaternion.
	 * @return first coordinate of the vectorial part of the quaternion
	 */
	public float getQ1() {
		return q1;
	}

	/** Get the second coordinate of the vectorial part of the quaternion.
	 * @return second coordinate of the vectorial part of the quaternion
	 */
	public float getQ2() {
		return q2;
	}

	/** Get the third coordinate of the vectorial part of the quaternion.
	 * @return third coordinate of the vectorial part of the quaternion
	 */
	public float getQ3() {
		return q3;
	}

	/** Get the normalized axis of the rotation.
	 * @return normalized axis of the rotation
	 */
	public PVector getAxis() {
		float squaredSine = q1 * q1 + q2 * q2 + q3 * q3;
		if (squaredSine == 0) {
			return new PVector(1, 0, 0);
		} 
		else if (q0 < 0) {
			float inverse = 1 / (float) Math.sqrt(squaredSine);
			return new PVector(q1 * inverse, q2 * inverse, q3 * inverse);
		}
		float inverse = -1 / (float) Math.sqrt(squaredSine);
		return new PVector(q1 * inverse, q2 * inverse, q3 * inverse);
	}

	/** Get the angle of the rotation.
	 * @return angle of the rotation (between 0 and &pi;)
	 */
	public float getAngle() {
		if ((q0 < -0.1) || (q0 > 0.1)) {
			return 2 * (float) Math.asin(Math.sqrt(q1 * q1 + q2 * q2 + q3 * q3));
		} 
		else if (q0 < 0) {
			return 2 * (float) Math.acos(-q0);
		}
		return 2 * (float) Math.acos(q0);
	}
	
	/** Get the Cardan or Euler angles corresponding to the instance.
	 * 
	 * <p>The equations show that each rotation can be defined by two
	 * different values of the Cardan or Euler angles set. For example
	 * if Cardan angles are used, the rotation defined by the angles
	 * a<sub>1</sub>, a<sub>2</sub> and a<sub>3</sub> is the same as
	 * the rotation defined by the angles &pi; + a<sub>1</sub>, &pi;
	 * - a<sub>2</sub> and &pi; + a<sub>3</sub>. This method implements
	 * the following arbitrary choices:</p>
	 * <ul>
	 *   <li>for Cardan angles, the chosen set is the one for which the
	 *   second angle is between -&pi;/2 and &pi;/2 (i.e its cosine is
	 *   positive),</li>
	 *   <li>for Euler angles, the chosen set is the one for which the
	 *   second angle is between 0 and &pi; (i.e its sine is positive).</li>
	 * </ul>
	 * 
	 * <p>Cardan and Euler angle have a very disappointing drawback: all
	 * of them have singularities. This means that if the instance is
	 * too close to the singularities corresponding to the given
	 * rotation order, it will be impossible to retrieve the angles. For
	 * Cardan angles, this is often called gimbal lock. There is
	 * <em>nothing</em> to do to prevent this, it is an intrinsic problem
	 * with Cardan and Euler representation (but not a problem with the
	 * rotation itself, which is perfectly well defined). For Cardan
	 * angles, singularities occur when the second angle is close to
	 * -&pi;/2 or +&pi;/2, for Euler angle singularities occur when the
	 * second angle is close to 0 or &pi;, this implies that the identity
	 * rotation is always singular for Euler angles!</p>
	 * 
	 * @param order rotation order to use
	 * @return an array of three angles, in the order specified by the set 
	 * or null if angle singularity found.
	 */
	public float[] getAngles(RotOrder order){

		if (order == RotOrder.XYZ) {

			// r (plusK) coordinates are :
			//  sin (theta), -cos (theta) sin (phi), cos (theta) cos (phi)
			// (-r) (plusI) coordinates are :
			// cos (psi) cos (theta), -sin (psi) cos (theta), sin (theta)
			// and we can choose to have theta in the interval [-PI/2 ; +PI/2]
			PVector v1 = applyToNew(PLUS_K);
			PVector v2 = applyInverseToNew(PLUS_I);
			if  ((v2.z < -0.9999999999) || (v2.z > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(-(v1.y), v1.z),
					(float) Math.asin(v2.z),
					(float) Math.atan2(-(v2.y), v2.x)
			};

		} else if (order == RotOrder.XZY) {

			// r (plusJ) coordinates are :
			// -sin (psi), cos (psi) cos (phi), cos (psi) sin (phi)
			// (-r) (plusI) coordinates are :
			// cos (theta) cos (psi), -sin (psi), sin (theta) cos (psi)
			// and we can choose to have psi in the interval [-PI/2 ; +PI/2]
			PVector v1 = applyToNew(PLUS_J);
			PVector v2 = applyInverseToNew(PLUS_I);
			if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.z, v1.y),
					-(float) Math.asin(v2.y),
					(float) Math.atan2(v2.z, v2.x)
			};

		} else if (order == RotOrder.YXZ) {

			// r (plusK) coordinates are :
			//  cos (phi) sin (theta), -sin (phi), cos (phi) cos (theta)
			// (-r) (plusJ) coordinates are :
			// sin (psi) cos (phi), cos (psi) cos (phi), -sin (phi)
			// and we can choose to have phi in the interval [-PI/2 ; +PI/2]
			PVector v1 = applyToNew(PLUS_K);
			PVector v2 = applyInverseToNew(PLUS_J);
			if ((v2.z < -0.9999999999) || (v2.z > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.x, v1.z),
					-(float) Math.asin(v2.z),
					(float) Math.atan2(v2.x, v2.y)
			};

		} else if (order == RotOrder.YZX) {

			// r (plusI) coordinates are :
			// cos (psi) cos (theta), sin (psi), -cos (psi) sin (theta)
			// (-r) (plusJ) coordinates are :
			// sin (psi), cos (phi) cos (psi), -sin (phi) cos (psi)
			// and we can choose to have psi in the interval [-PI/2 ; +PI/2]
			PVector v1 = applyToNew(PLUS_I);
			PVector v2 = applyInverseToNew(PLUS_J);
			if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(-(v1.z), v1.x),
					(float) Math.asin(v2.x),
					(float) Math.atan2(-(v2.z), v2.y)
			};

		} else if (order == RotOrder.ZXY) {

			// r (plusJ) coordinates are :
			// -cos (phi) sin (psi), cos (phi) cos (psi), sin (phi)
			// (-r) (plusK) coordinates are :
			// -sin (theta) cos (phi), sin (phi), cos (theta) cos (phi)
			// and we can choose to have phi in the interval [-PI/2 ; +PI/2]
			PVector v1 = applyToNew(PLUS_J);
			PVector v2 = applyInverseToNew(PLUS_K);
			if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(-(v1.x), v1.y),
					(float) Math.asin(v2.y),
					(float) Math.atan2(-(v2.x), v2.z)
			};

		} else if (order == RotOrder.ZYX) {

			// r (plusI) coordinates are :
			//  cos (theta) cos (psi), cos (theta) sin (psi), -sin (theta)
			// (-r) (plusK) coordinates are :
			// -sin (theta), sin (phi) cos (theta), cos (phi) cos (theta)
			// and we can choose to have theta in the interval [-PI/2 ; +PI/2]
			PVector v1 = applyToNew(PLUS_I);
			PVector v2 = applyInverseToNew(PLUS_K);
			if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.y, v1.x),
					-(float) Math.asin(v2.x),
					(float) Math.atan2(v2.y, v2.z)
			};

		} else if (order == RotOrder.XYX) {

			// r (plusI) coordinates are :
			//  cos (theta), sin (phi1) sin (theta), -cos (phi1) sin (theta)
			// (-r) (plusI) coordinates are :
			// cos (theta), sin (theta) sin (phi2), sin (theta) cos (phi2)
			// and we can choose to have theta in the interval [0 ; PI]
			PVector v1 = applyToNew(PLUS_I);
			PVector v2 = applyInverseToNew(PLUS_I);
			if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.y, -v1.z),
					(float) Math.acos(v2.x),
					(float) Math.atan2(v2.y, v2.z)
			};

		} else if (order == RotOrder.XZX) {

			// r (plusI) coordinates are :
			//  cos (psi), cos (phi1) sin (psi), sin (phi1) sin (psi)
			// (-r) (plusI) coordinates are :
			// cos (psi), -sin (psi) cos (phi2), sin (psi) sin (phi2)
			// and we can choose to have psi in the interval [0 ; PI]
			PVector v1 = applyToNew(PLUS_I);
			PVector v2 = applyInverseToNew(PLUS_I);
			if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.z, v1.y),
					(float) Math.acos(v2.x),
					(float) Math.atan2(v2.z, -v2.y)
			};

		} else if (order == RotOrder.YXY) {

			// r (plusJ) coordinates are :
			//  sin (theta1) sin (phi), cos (phi), cos (theta1) sin (phi)
			// (-r) (plusJ) coordinates are :
			// sin (phi) sin (theta2), cos (phi), -sin (phi) cos (theta2)
			// and we can choose to have phi in the interval [0 ; PI]
			PVector v1 = applyToNew(PLUS_J);
			PVector v2 = applyInverseToNew(PLUS_J);
			if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.x, v1.z),
					(float) Math.acos(v2.y),
					(float) Math.atan2(v2.x, -v2.z)
			};

		} else if (order == RotOrder.YZY) {

			// r (plusJ) coordinates are :
			//  -cos (theta1) sin (psi), cos (psi), sin (theta1) sin (psi)
			// (-r) (plusJ) coordinates are :
			// sin (psi) cos (theta2), cos (psi), sin (psi) sin (theta2)
			// and we can choose to have psi in the interval [0 ; PI]
			PVector v1 = applyToNew(PLUS_J);
			PVector v2 = applyInverseToNew(PLUS_J);
			if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.z, -v1.x),
					(float) Math.acos(v2.y),
					(float) Math.atan2(v2.z, v2.x)
			};

		} else if (order == RotOrder.ZXZ) {

			// r (plusK) coordinates are :
			//  sin (psi1) sin (phi), -cos (psi1) sin (phi), cos (phi)
			// (-r) (plusK) coordinates are :
			// sin (phi) sin (psi2), sin (phi) cos (psi2), cos (phi)
			// and we can choose to have phi in the interval [0 ; PI]
			PVector v1 = applyToNew(PLUS_K);
			PVector v2 = applyInverseToNew(PLUS_K);
			if ((v2.z < -0.9999999999) || (v2.z > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.x, -v1.y),
					(float) Math.acos(v2.z),
					(float) Math.atan2(v2.x, v2.y)
			};

		} else { // last possibility is ZYZ

			// r (plusK) coordinates are :
			//  cos (psi1) sin (theta), sin (psi1) sin (theta), cos (theta)
			// (-r) (plusK) coordinates are :
			// -sin (theta) cos (psi2), sin (theta) sin (psi2), cos (theta)
			// and we can choose to have theta in the interval [0 ; PI]
			PVector v1 = applyToNew(PLUS_K);
			PVector v2 = applyInverseToNew(PLUS_K);
			if ((v2.z < -0.9999999999) || (v2.z > 0.9999999999)) {
				return null;
			}
			return new float[] {
					(float) Math.atan2(v1.y, v1.x),
					(float) Math.acos(v2.z),
					(float) Math.atan2(v2.y, -v2.x)
			};
		}
	}


	/** Get the 3X3 matrix corresponding to the instance
	 * @return the matrix corresponding to the instance
	 */
	public float[][] getMatrix() {

		// products
		float q0q0  = q0 * q0;
		float q0q1  = q0 * q1;
		float q0q2  = q0 * q2;
		float q0q3  = q0 * q3;
		float q1q1  = q1 * q1;
		float q1q2  = q1 * q2;
		float q1q3  = q1 * q3;
		float q2q2  = q2 * q2;
		float q2q3  = q2 * q3;
		float q3q3  = q3 * q3;

		// create the matrix
		float[][] m = new float[3][];
		m[0] = new float[3];
		m[1] = new float[3];
		m[2] = new float[3];

		m [0][0] = 2.0f * (q0q0 + q1q1) - 1.0f;
		m [1][0] = 2.0f * (q1q2 - q0q3);
		m [2][0] = 2.0f * (q1q3 + q0q2);

		m [0][1] = 2.0f * (q1q2 + q0q3);
		m [1][1] = 2.0f * (q0q0 + q2q2) - 1.0f;
		m [2][1] = 2.0f * (q2q3 - q0q1);

		m [0][2] = 2.0f * (q1q3 - q0q2);
		m [1][2] = 2.0f * (q2q3 + q0q1);
		m [2][2] = 2.0f * (q0q0 + q3q3) - 1.0f;

		return m;

	}

	/** Apply the rotation to a vector.
	 * @param u vector to apply the rotation to
	 * @return the vector u after rotation
	 */
	public PVector applyTo(PVector u) {

		float x = u.x;
		float y = u.y;
		float z = u.z;

		float s = q1 * x + q2 * y + q3 * z;

		float nx = 2 * (q0 * (x * q0 - (q2 * z - q3 * y)) + s * q1) - x;
		float ny = 2 * (q0 * (y * q0 - (q3 * x - q1 * z)) + s * q2) - y;
		float nz = 2 * (q0 * (z * q0 - (q1 * y - q2 * x)) + s * q3) - z;

		u.x = nx;
		u.y = ny;
		u.z = nz;
		
		return u;
	}

	/**
	 * Same as applyTo but u is unchanged and a new PVector is returned.
	 * @param u
	 */
	public PVector applyToNew(final PVector u) {

		float x = u.x;
		float y = u.y;
		float z = u.z;

		float s = q1 * x + q2 * y + q3 * z;

		float nx = 2 * (q0 * (x * q0 - (q2 * z - q3 * y)) + s * q1) - x;
		float ny = 2 * (q0 * (y * q0 - (q3 * x - q1 * z)) + s * q2) - y;
		float nz = 2 * (q0 * (z * q0 - (q1 * y - q2 * x)) + s * q3) - z;

		return new PVector(nx,ny,nz);
	}

	/** Apply the inverse of the rotation to a vector.
	 * @param u vector to apply the inverse of the rotation to
	 * @return a new vector which such that u is its image by the rotation
	 */
	public PVector applyInverseTo(PVector u) {

		float x = u.x;
		float y = u.y;
		float z = u.z;

		float s = q1 * x + q2 * y + q3 * z;
		float m0 = -q0;

		float nx = 2 * (m0 * (x * m0 - (q2 * z - q3 * y)) + s * q1) - x;
		float ny = 2 * (m0 * (y * m0 - (q3 * x - q1 * z)) + s * q2) - y;
		float nz = 2 * (m0 * (z * m0 - (q1 * y - q2 * x)) + s * q3) - z;
		
		u.x = nx;
		u.y = ny;
		u.z = nz;
		
		return u;
	}

	/**
	 * Same as applyInverseTo but u is unchanged and a new PVector is returned.
	 * @param u
	 */
	public PVector applyInverseToNew(PVector u) {

		float x = u.x;
		float y = u.y;
		float z = u.z;

		float s = q1 * x + q2 * y + q3 * z;
		float m0 = -q0;

		float nx = 2 * (m0 * (x * m0 - (q2 * z - q3 * y)) + s * q1) - x;
		float ny = 2 * (m0 * (y * m0 - (q3 * x - q1 * z)) + s * q2) - y;
		float nz = 2 * (m0 * (z * m0 - (q1 * y - q2 * x)) + s * q3) - z;
		
		return new PVector(nx, ny, nz);
	}

	/** Apply the instance to another rotation.
	 * Applying the instance to a rotation is computing the composition
	 * in an order compliant with the following rule : let u be any
	 * vector and v its image by r (i.e. r.applyTo(u) = v), let w be the image
	 * of v by the instance (i.e. applyTo(v) = w), then w = comp.applyTo(u),
	 * where comp = applyTo(r).
	 * @param r rotation to apply the rotation to
	 * @return a new rotation which is the composition of r by the instance
	 */
	public Rot applyTo(Rot r) {
		return new Rot(r.q0 * q0 - (r.q1 * q1 + r.q2 * q2 + r.q3 * q3),
				r.q1 * q0 + r.q0 * q1 + (r.q2 * q3 - r.q3 * q2),
				r.q2 * q0 + r.q0 * q2 + (r.q3 * q1 - r.q1 * q3),
				r.q3 * q0 + r.q0 * q3 + (r.q1 * q2 - r.q2 * q1),
				false);
	}

	/** Apply the inverse of the instance to another rotation.
	 * Applying the inverse of the instance to a rotation is computing
	 * the composition in an order compliant with the following rule :
	 * let u be any vector and v its image by r (i.e. r.applyTo(u) = v),
	 * let w be the inverse image of v by the instance
	 * (i.e. applyInverseTo(v) = w), then w = comp.applyTo(u), where
	 * comp = applyInverseTo(r).
	 * @param r rotation to apply the rotation to
	 * @return a new rotation which is the composition of r by the inverse
	 * of the instance
	 */
	public Rot applyInverseTo(Rot r) {
		return new Rot(-r.q0 * q0 - (r.q1 * q1 + r.q2 * q2 + r.q3 * q3),
				-r.q1 * q0 + r.q0 * q1 + (r.q2 * q3 - r.q3 * q2),
				-r.q2 * q0 + r.q0 * q2 + (r.q3 * q1 - r.q1 * q3),
				-r.q3 * q0 + r.q0 * q3 + (r.q1 * q2 - r.q2 * q1),
				false);
	}

	/** Perfect orthogonality on a 3X3 matrix.
	 * @param m initial matrix (not exactly orthogonal)
	 * @param threshold convergence threshold for the iterative
	 * orthogonality correction (convergence is reached when the
	 * difference between two steps of the Frobenius norm of the
	 * correction is below this threshold)
	 * @return an orthogonal matrix close to m
	 * @exception NotARotationMatrixException if the matrix cannot be
	 * orthogonalized with the given threshold after 10 iterations
	 */
	private float[][] orthogonalizeMatrix(float[][] m, float threshold){
		float[] m0 = m[0];
		float[] m1 = m[1];
		float[] m2 = m[2];
		float x00 = m0[0];
		float x01 = m0[1];
		float x02 = m0[2];
		float x10 = m1[0];
		float x11 = m1[1];
		float x12 = m1[2];
		float x20 = m2[0];
		float x21 = m2[1];
		float x22 = m2[2];
		float fn = 0;
		float fn1;

		float[][] orth = new float[3][3];
		float[] o0 = orth[0];
		float[] o1 = orth[1];
		float[] o2 = orth[2];

		// iterative correction: Xn+1 = Xn - 0.5 * (Xn.Mt.Xn - M)
		int i = 0;
		while (++i < 11) {

			// Mt.Xn
			float mx00 = m0[0] * x00 + m1[0] * x10 + m2[0] * x20;
			float mx10 = m0[1] * x00 + m1[1] * x10 + m2[1] * x20;
			float mx20 = m0[2] * x00 + m1[2] * x10 + m2[2] * x20;
			float mx01 = m0[0] * x01 + m1[0] * x11 + m2[0] * x21;
			float mx11 = m0[1] * x01 + m1[1] * x11 + m2[1] * x21;
			float mx21 = m0[2] * x01 + m1[2] * x11 + m2[2] * x21;
			float mx02 = m0[0] * x02 + m1[0] * x12 + m2[0] * x22;
			float mx12 = m0[1] * x02 + m1[1] * x12 + m2[1] * x22;
			float mx22 = m0[2] * x02 + m1[2] * x12 + m2[2] * x22;

			// Xn+1
			o0[0] = x00 - 0.5f * (x00 * mx00 + x01 * mx10 + x02 * mx20 - m0[0]);
			o0[1] = x01 - 0.5f * (x00 * mx01 + x01 * mx11 + x02 * mx21 - m0[1]);
			o0[2] = x02 - 0.5f * (x00 * mx02 + x01 * mx12 + x02 * mx22 - m0[2]);
			o1[0] = x10 - 0.5f * (x10 * mx00 + x11 * mx10 + x12 * mx20 - m1[0]);
			o1[1] = x11 - 0.5f * (x10 * mx01 + x11 * mx11 + x12 * mx21 - m1[1]);
			o1[2] = x12 - 0.5f * (x10 * mx02 + x11 * mx12 + x12 * mx22 - m1[2]);
			o2[0] = x20 - 0.5f * (x20 * mx00 + x21 * mx10 + x22 * mx20 - m2[0]);
			o2[1] = x21 - 0.5f * (x20 * mx01 + x21 * mx11 + x22 * mx21 - m2[1]);
			o2[2] = x22 - 0.5f * (x20 * mx02 + x21 * mx12 + x22 * mx22 - m2[2]);

			// correction on each elements
			float corr00 = o0[0] - m0[0];
			float corr01 = o0[1] - m0[1];
			float corr02 = o0[2] - m0[2];
			float corr10 = o1[0] - m1[0];
			float corr11 = o1[1] - m1[1];
			float corr12 = o1[2] - m1[2];
			float corr20 = o2[0] - m2[0];
			float corr21 = o2[1] - m2[1];
			float corr22 = o2[2] - m2[2];

			// Frobenius norm of the correction
			fn1 = corr00 * corr00 + corr01 * corr01 + corr02 * corr02 +
			corr10 * corr10 + corr11 * corr11 + corr12 * corr12 +
			corr20 * corr20 + corr21 * corr21 + corr22 * corr22;

			// convergence test
			if (Math.abs(fn1 - fn) <= threshold)
				return orth;

			// prepare next iteration
			x00 = o0[0];
			x01 = o0[1];
			x02 = o0[2];
			x10 = o1[0];
			x11 = o1[1];
			x12 = o1[2];
			x20 = o2[0];
			x21 = o2[1];
			x22 = o2[2];
			fn  = fn1;
		}
		return null;
	}

	/** Compute the <i>distance</i> between two rotations.
	 * <p>The <i>distance</i> is intended here as a way to check if two
	 * rotations are almost similar (i.e. they transform vectors the same way)
	 * or very different. It is mathematically defined as the angle of
	 * the rotation r that prepended to one of the rotations gives the other
	 * one:</p>
	 * <pre>
	 *        r<sub>1</sub>(r) = r<sub>2</sub>
	 * </pre>
	 * <p>This distance is an angle between 0 and &pi;. Its value is the smallest
	 * possible upper bound of the angle in radians between r<sub>1</sub>(v)
	 * and r<sub>2</sub>(v) for all possible vectors v. This upper bound is
	 * reached for some v. The distance is equal to 0 if and only if the two
	 * rotations are identical.</p>
	 * <p>Comparing two rotations should always be done using this value rather
	 * than for example comparing the components of the quaternions. It is much
	 * more stable, and has a geometric meaning. Also comparing quaternions
	 * components is error prone since for example quaternions (0.36, 0.48, -0.48, -0.64)
	 * and (-0.36, -0.48, 0.48, 0.64) represent exactly the same rotation despite
	 * their components are different (they are exact opposites).</p>
	 * @param r1 first rotation
	 * @param r2 second rotation
	 * @return <i>distance</i> between r1 and r2
	 */
	public static float distance(Rot r1, Rot r2) {
		return r1.applyInverseTo(r2).getAngle();
	}

	public String toString(){
		return "Q["+q0+" "+q1+" "+q2+" "+q3+"] ";
	}
}
