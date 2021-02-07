package games.core

trait Vec

case class Vec2(x: Int, y: Int) extends Vec {

  def + (v: Vec2) = Vec2(x + v.x, y + v.y)
  def + (v: (Int, Int)) = Vec2(x + v._1, y + v._2)
  def - (v: Vec2) = Vec2(x - v.x, y - v.y)
  def * (d: Int) = Vec2(x * d, y * d)
  def / (d: Int) = Vec2(x / d, y / d)
  def unary_- = Vec2(-x, -y)

  def directionTo(v: Vec2) = {
    def gcd(a: Int, b: Int): Int =
      if (b == 0) a else gcd(b, a % b)
    if (v == this) Vec2.Zero else
    (v - this) / gcd((v.x - x).abs, (v.y - y).abs)
  }

  def stepsTo(v: Vec2) = {
    val dir = this directionTo v
    if (dir == Vec2.Zero) 0
    else if (dir.y == 0) (v - this).x / dir.x
    else (v - this).y / dir.y
  }
}

object Vec2 {

  val Zero = Vec2(0, 0)
  
  val N = Vec2(0, 1)
  val E = Vec2(1, 0)
  val S = Vec2(0, -1)
  val W = Vec2(-1, 0)

  val NE = Vec2(1, 1)
  val SE = Vec2(1, -1)
  val SW = Vec2(-1, -1)
  val NW = Vec2(-1, 1)

  val orthogonal = Seq(N, E, S, W)
  val diagonal = Seq(NE, SE, SW, NW)
  val cardinal = orthogonal ++ diagonal
}