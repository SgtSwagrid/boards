package games.core

import scala.math._
import games.core.{Vec, Vec2}

trait Manifold[V <: Vec] {

    val positions: Seq[V]
    def inBounds(pos: V): Boolean
    val area: Int
  }

object Manifold {

  trait Rectangular extends Manifold[Vec2] {

    def line(start: Vec2, end: Vec2): Iterable[Vec2] = {
      val dir = start directionTo end
      val size = (start stepsTo end) + 1
      ray(start, dir, size)
    }

    def linesThrough(pos: Vec2, dir: Vec2, length: Int): Iterable[Iterable[Vec2]] = {
      (0 until length).map { l => 
        ray(pos, dir, l) ++ Seq(pos) ++ ray(pos, -dir, length - l)
      }
    }

    def ray(start: Vec2, dir: Vec2): Iterable[Vec2] = {
      LazyList.iterate(start + dir)(_ + dir).takeWhile(inBounds)
    }

    def ray(start: Vec2, dir: Vec2, size: Int): Iterable[Vec2] = {
      ray(start, dir).take(size)
    }

    def rayUntil(start: Vec2, dir: Vec2, end: Vec2 => Boolean): Iterable[Vec2] = {
      ray(start, dir).takeWhile(!end(_))
    }

    def rayTo(start: Vec2, dir: Vec2, end: Vec2 => Boolean): Iterable[Vec2] = {
      ray(start, dir).span(!end(_)) match {
        case (prefix, suffix) => prefix ++ suffix.headOption
      }
    }

    def box(centre: Vec2, r: Int): Iterable[Vec2] = {

      val box = for {
        x <- -r to r
        y <- if (x.abs == r) -r to r else Seq(-r, r)
      } yield Vec2(x, y)

      box.view.map(_ + centre).filter(inBounds)
    }

    def evenBox(centre: Vec2, r: Int): Iterable[Vec2] = {

      val box = for {
        x <- -r - 1 to r
        y <- if (x == r || x == -r-1) -r-1 to r else Seq(-r-1, r)
      } yield Vec2(x, y)

      box.view.map(_ + centre).filter(inBounds)
    }

    def diamond(centre: Vec2, r: Int): Iterable[Vec2] = {

      val diamond = for {
        x <- -r to r
        y <- Set(r - x.abs, x.abs - r)
      } yield Vec2(x, y)

      diamond.view.map(_ + centre).filter(inBounds)
    }

    def evenDiamond(centre: Vec2, r: Int): Iterable[Vec2] = {

      val diamond = for {
        x <- -r-1 to r
        y <- {
          val h = if (x >= 0) x else -x-1
          Seq(r - h, h - r-1)
        }
      } yield Vec2(x, y)

      diamond.view.map(_ + centre).filter(inBounds)
    }
  }

  case class Rectangle(width: Int, height: Int) extends Rectangular {

    val size = Vec2(width, height)
    val area = width * height

    val positions = for {
      x <- 0 until width
      y <- 0 until height
    } yield Vec2(x, y)

    def inBounds(pos: Vec2) =
      pos.x >= 0 && pos.x < width &&
      pos.y >= 0 && pos.y < height

    def taxiDist(p1: Vec2, p2: Vec2) =
      abs(p2.x - p1.x) + abs(p2.y - p1.y)

    def kingsDist(p1: Vec2, p2: Vec2) =
      abs(p2.x - p1.x) max abs(p2.y - p1.y)

    def orthAdjacent(p1: Vec2, p2: Vec2) =
      taxiDist(p1, p2) == 1

    def diagAdjacent(p1: Vec2, p2: Vec2) =
      kingsDist(p1, p2) == 1

    def row(y: Int) =
      (0 until width).map(x => Vec2(x, y))

    def column(x: Int) =
      (0 until height).map(y => Vec2(x, y))
  }

  case class Rows(rows: Row*) extends Rectangular {

    val area: Int = rows.map(_.area).sum

    val positions = for {
      (row, y) <- rows.zipWithIndex
      x <- row.positions
    } yield Vec2(x, y)

    def inBounds(pos: Vec2) =
      pos.y >= 0 && pos.y < rows.size &&
      rows(pos.y).inBounds(pos.x)
    
    def row(y: Int) =
      rows(y).positions.map(x => Vec2(x, y))
    
    def column(x: Int) =
      (0 until rows.size)
        .filter(y => rows(y).inBounds(x))
        .map(y => Vec2(x, y))
  }

  case class Row(sections: Section*) {

    val area = sections.map(_.size).sum

    val offsets = (Section(0) +: sections).sliding(2).map {
      case Seq(l, r) => l.size + r.offset
    }.scanLeft(0)(_+_).toSeq.tail

    val positions = for {
      (section, offset) <- (sections zip offsets)
      x <- (0 until section.size).map(_ + offset)
    } yield x

    def inBounds(x: Int) = (sections zip offsets)
      .dropWhile { case (_, o) => o > x }
      .headOption.exists { case (s, o) => x - o < s.size }

    val start = sections.head.offset
    val end = sections.map(s => s.offset + s.size).sum
  }

  case class Section(size: Int = 1, offset: Int = 0)
}