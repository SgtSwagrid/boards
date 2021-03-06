package games.core

trait Piece {
  val ownerId: Int
  val texture: String
  def byOwner[T](options: T*) = options(ownerId)
}

object Piece {

  trait Placeable[V <: Vec, P <: Piece] extends Piece {
    def places(state: State[V, P]): ActionSet[V, P, Action.Place[V]]
  }

  trait Moveable[V <: Vec, P <: Piece] extends Piece {
    def moves(state: State[V, P], pos: V): ActionSet[V, P, Action.Move[V]]
  }
}