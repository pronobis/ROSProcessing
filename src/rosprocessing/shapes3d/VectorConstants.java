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
 * This interface has been included to add various constants used in the Rot
 * class but are not part of the PVector class.
 * 
 * @author Peter Lager
 *
 */
public interface VectorConstants {

	  /** Null vector (coordinates: 0, 0, 0). */
	  public static final PVector ZERO   = new PVector(0, 0, 0);

	  /** First canonical vector (coordinates: 1, 0, 0). */
	  public static final PVector PLUS_I = new PVector(1, 0, 0);

	  /** Opposite of the first canonical vector (coordinates: -1, 0, 0). */
	  public static final PVector MINUS_I = new PVector(-1, 0, 0);

	  /** Second canonical vector (coordinates: 0, 1, 0). */
	  public static final PVector PLUS_J = new PVector(0, 1, 0);

	  /** Opposite of the second canonical vector (coordinates: 0, -1, 0). */
	  public static final PVector MINUS_J = new PVector(0, -1, 0);

	  /** Third canonical vector (coordinates: 0, 0, 1). */
	  public static final PVector PLUS_K = new PVector(0, 0, 1);

	  /** Opposite of the third canonical vector (coordinates: 0, 0, -1).  */
	  public static final PVector MINUS_K = new PVector(0, 0, -1);

	  /** A vector with all coordinates set to NaN. */
	  public static final PVector NaN = new PVector(Float.NaN, Float.NaN, Float.NaN);

	  /** A vector with all coordinates set to positive infinity. */
	  public static final PVector POSITIVE_INFINITY =
	      new PVector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

	  /** A vector with all coordinates set to negative infinity. */
	  public static final PVector NEGATIVE_INFINITY =
	      new PVector(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

}
