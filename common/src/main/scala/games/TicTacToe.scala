package games

import games.core.Game
import games.core.States._
import games.core.Pieces._
import games.core.Actions._
import games.core.Manifolds._
import games.core.Coordinates._
import games.core.Layouts._
import games.core.Backgrounds._
import games.core.Colour

class TicTacToe(id: Int) extends Game(id) {

  val name = "Tic Tac Toe"
  val players = Seq(2)

  sealed trait TicTacToePiece extends Piece

  type Vec = Vec2
  type StateT = State[TicTacToePiece, Vec, Null]
  
  val manifold = RectangleManifold(3, 3)
  val layout = GridLayout
  val background = Checkerboard(Colour.white, Colour.black)

  val start = State()
}