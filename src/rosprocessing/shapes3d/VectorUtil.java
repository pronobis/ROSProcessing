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

package rosprocessing;


import processing.core.PVector;

/**
 * This class is used internally by the library and provides some
 * additional methods not covered in the PVector class. <br>
 * 
 * @author Peter Lager
 *
 */
public class VectorUtil {

	private static final float ACCURACY = 1e-5f;

	/**
	 * The algorithm for this method was taken from the Vector3D class that is
	 * part of the Apache commons math library at http://commons.apache.org/math/ <br>
	 * 
	 * @param v
	 * @return the orthogonal vector
	 */
	public static PVector orthogonal(PVector v) {
		float threshold, inverse;
		threshold = 0.6f * v.mag();
		if (threshold == 0) {
                  //System.out.println(Messages.build("Vector {0} has zero magnitude", v));
			return null;
		}

		if ((v.x >= -threshold) && (v.x <= threshold)) {
			inverse  = 1 / (float)Math.sqrt(v.y * v.y + v.z * v.z);
			return new PVector(0, inverse * v.z, -inverse * v.y);
		} 
		else if ((v.y >= -threshold) && (v.y <= threshold)) {
			inverse  = 1 / (float)Math.sqrt(v.x * v.x + v.z * v.z);
			return new PVector(-inverse * v.z, 0, inverse * v.x);
		}

		inverse  = 1 / (float)Math.sqrt(v.x * v.x + v.y * v.y);
		return new PVector(inverse * v.y, -inverse * v.x, 0);
	}

	/**
	 * Compares a PVector with a set of 3D coordinates. They are considered
	 * the same if the difference is less than 1e-5f for each value (x,y & z)
	 * @param v0
	 * @param x
	 * @param y
	 * @param z
	 * @return true if the coordinates are the same
	 */
	public static boolean same(PVector v0, float x, float y, float z){
		boolean result = (Math.abs(v0.x - x) < ACCURACY) &&
		(Math.abs(v0.y - y) < ACCURACY) &&
		(Math.abs(v0.z - z) < ACCURACY);
		return result;
	}

	/**
	 * Compares 2 PVectors. They are considered the same if the difference 
	 * is less than 1e-5f for each attribute (x,y & z)
	 * @param v0
	 * @param v1
	 * @return true if the vectors are considered to be the same are the same
	 */
	public static boolean same(PVector v0, PVector v1){
		boolean result = (Math.abs(v0.x - v1.x) < ACCURACY) &&
		(Math.abs(v0.y - v1.y) < ACCURACY) &&
		(Math.abs(v0.z - v1.z) < ACCURACY);
		return result;
	}

	/**
	 * Return a copy of the original vector. Effectively performs the action
	 * of the copy constructor missing from PVector.
	 * @param v
	 * @return a new PVector that is a copy
	 */
	public static PVector getCopy(PVector v){
		if(v == null)
			return null;
		else
			return new PVector(v.x, v.y, v.z);
	}
	
}
