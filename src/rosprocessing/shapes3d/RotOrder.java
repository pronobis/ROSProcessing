package rosprocessing;

import processing.core.PVector;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/**
 * This class is a utility representing a rotation order specification
 * for Cardan or Euler angles specification.
 *
 * This class cannot be instantiated by the user. He can only use one
 * of the twelve predefined supported orders as an argument to either
 * the {@link Rot#Rot(RotOrder,float,float,float)}
 * constructor or the {@link Rot#getAngles} method.
 *
 * @version $Revision: 762087 $ $Date: 2009-04-05 10:20:18 -0400 (Sun, 05 Apr 2009) $
 * @since 1.2
 */
public final class RotOrder implements VectorConstants {

  /** Private constructor.
   * This is a utility class that cannot be instantiated by the user,
   * so its only constructor is private.
   * @param name name of the rotation order
   * @param a1 axis of the first rotation
   * @param a2 axis of the second rotation
   * @param a3 axis of the third rotation
   */
  private RotOrder(String name,
                        PVector a1, PVector a2, PVector a3) {
    this.name = name;
    this.a1   = a1;
    this.a2   = a2;
    this.a3   = a3;
  }

  /** Get a string representation of the instance.
   * @return a string representation of the instance (in fact, its name)
   */
  @Override
  public String toString() {
    return name;
  }

  /** Get the axis of the first rotation.
   * @return axis of the first rotation
   */
  public PVector getA1() {
    return a1;
  }

  /** Get the axis of the second rotation.
   * @return axis of the second rotation
   */
  public PVector getA2() {
    return a2;
  }

  /** Get the axis of the second rotation.
   * @return axis of the second rotation
   */
  public PVector getA3() {
    return a3;
  }

  /** Set of Cardan angles.
   * this ordered set of rotations is around X, then around Y, then
   * around Z
   */
  public static final RotOrder XYZ =
    new RotOrder("XYZ", PLUS_I, PLUS_J, PLUS_K);

  /** Set of Cardan angles.
   * this ordered set of rotations is around X, then around Z, then
   * around Y
   */
  public static final RotOrder XZY =
    new RotOrder("XZY", PLUS_I, PLUS_K, PLUS_J);

  /** Set of Cardan angles.
   * this ordered set of rotations is around Y, then around X, then
   * around Z
   */
  public static final RotOrder YXZ =
    new RotOrder("YXZ", PLUS_J, PLUS_I, PLUS_K);

  /** Set of Cardan angles.
   * this ordered set of rotations is around Y, then around Z, then
   * around X
   */
  public static final RotOrder YZX =
    new RotOrder("YZX", PLUS_J, PLUS_K, PLUS_I);

  /** Set of Cardan angles.
   * this ordered set of rotations is around Z, then around X, then
   * around Y
   */
  public static final RotOrder ZXY =
    new RotOrder("ZXY", PLUS_K, PLUS_I, PLUS_J);

  /** Set of Cardan angles.
   * this ordered set of rotations is around Z, then around Y, then
   * around X
   */
  public static final RotOrder ZYX =
    new RotOrder("ZYX", PLUS_K, PLUS_J, PLUS_I);

  /** Set of Euler angles.
   * this ordered set of rotations is around X, then around Y, then
   * around X
   */
  public static final RotOrder XYX =
    new RotOrder("XYX", PLUS_I, PLUS_J, PLUS_I);

  /** Set of Euler angles.
   * this ordered set of rotations is around X, then around Z, then
   * around X
   */
  public static final RotOrder XZX =
    new RotOrder("XZX", PLUS_I, PLUS_K, PLUS_I);

  /** Set of Euler angles.
   * this ordered set of rotations is around Y, then around X, then
   * around Y
   */
  public static final RotOrder YXY =
    new RotOrder("YXY", PLUS_J, PLUS_I, PLUS_J);

  /** Set of Euler angles.
   * this ordered set of rotations is around Y, then around Z, then
   * around Y
   */
  public static final RotOrder YZY =
    new RotOrder("YZY", PLUS_J, PLUS_K, PLUS_J);

  /** Set of Euler angles.
   * this ordered set of rotations is around Z, then around X, then
   * around Z
   */
  public static final RotOrder ZXZ =
    new RotOrder("ZXZ", PLUS_K, PLUS_I, PLUS_K);

  /** Set of Euler angles.
   * this ordered set of rotations is around Z, then around Y, then
   * around Z
   */
  public static final RotOrder ZYZ =
    new RotOrder("ZYZ", PLUS_K, PLUS_J, PLUS_K);

  /** Name of the rotations order. */
  private final String name;

  /** Axis of the first rotation. */
  private final PVector a1;

  /** Axis of the second rotation. */
  private final PVector a2;

  /** Axis of the third rotation. */
  private final PVector a3;

}
