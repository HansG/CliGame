package clig

import cats.syntax.all.*
import clig.*

enum GameError(msg: String) extends Throwable(msg):
  case FieldAlreadyTaken(c:  Coordinate , p: Player) extends GameError(show"The field $c is already taken by $p.")
  case WrongPlayer(c: Coordinate, p: Player) extends GameError(show"Wrong player: $p.")
  case CoordinateOutOfBound(c: Coordinate) extends GameError(show"Coordinates are out of bounds: $c (${c.x}, ${c.y}).")
  case GameIsOver(winner: Option[Player]) extends GameError(winner.fold("Game was drawn")(p => show"Game was already won by $p."))

enum GameStatus:
  case Ongoing(nextPlayer: Player)
  case Won(winner: Player)
  case Drawn


def create: Game = Game(Board.create, GameStatus.Ongoing(Player.X), 0)

final case class Game(fields: Board, status: GameStatus, moves: Int):
  def move(c: Coordinate, p: Player): Either[GameError, Game] =
    status match
      case GameStatus.Won(winner) => GameError.GameIsOver(winner.some).asLeft
      case GameStatus.Drawn           => GameError.GameIsOver(none).asLeft
      case GameStatus.Ongoing(nextPlayer) =>
        fields(c) match
          case Some(FieldStatus.Empty) =>
            if nextPlayer === p then
              fields
                .update(c, FieldStatus.Taken(p))
                .map(updated =>
                  process(copy(fields = updated, moves = moves + 1), p, c)
                )
                .toRight(GameError.CoordinateOutOfBound(c))
            else GameError.WrongPlayer(c, p).asLeft
          case Some(FieldStatus.Taken(_)) =>
            GameError.FieldAlreadyTaken(c, p).asLeft
          case None => GameError.CoordinateOutOfBound(c).asLeft

def process(g : Game,p: Player, c: Coordinate ) = ???




