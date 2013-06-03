/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.geom;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents a line
 * @author Thomas Cashman
 */
public class Line {
	private Vector2 intersection;
	private Vector2 p1, p2;

	public Line() {
		this(0, 0, 0, 0);
	}

	public Line(float x1, float y1, float x2, float y2) {
		p1 = new Vector2(x1, y1);
		p2 = new Vector2(x2, y2);
		intersection = new Vector2();
	}
	
	public void set(float x1, float y1, float x2, float y2) {
		p1.set(x1, y1);
		p2.set(x2, y2);
	}

	public boolean intersects(Line line) {
		return Intersector.intersectLines(p1, p2, line.getP1(), line.getP2(),
				intersection);
	}

	public Vector2 getIntersection(Line line) {
		if(!Intersector.intersectLines(p1, p2, line.getP1(), line.getP2(),
				intersection)) {
			return null;
		}
		return intersection;
	}

	public boolean intersects(Rectangle rectangle) {
		return rectangle.getTopSide().intersects(this)
				|| rectangle.getBottomSide().intersects(this)
				|| rectangle.getLeftSide().intersects(this)
				|| rectangle.getRightSide().intersects(this);
	}
	
	public List<Vector2> getIntersections(Rectangle rectangle) {
		List<Vector2> result = new ArrayList<Vector2>();
		Vector2 intersection = rectangle.getTopSide().getIntersection(this);
		if(intersection != null) {
			result.add(intersection);
		}
		intersection = rectangle.getBottomSide().getIntersection(this);
		if(intersection != null) {
			result.add(intersection);
		}
		intersection = rectangle.getLeftSide().getIntersection(this);
		if(intersection != null) {
			result.add(intersection);
		}
		intersection = rectangle.getRightSide().getIntersection(this);
		if(intersection != null) {
			result.add(intersection);
		}
		return result;
	}

	public Vector2 getP1() {
		return p1;
	}

	public void setP1(Vector2 p1) {
		this.p1 = p1;
	}

	public Vector2 getP2() {
		return p2;
	}

	public void setP2(Vector2 p2) {
		this.p2 = p2;
	}
}
