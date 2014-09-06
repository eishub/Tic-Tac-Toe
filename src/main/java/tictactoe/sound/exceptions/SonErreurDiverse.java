package tictactoe.sound.exceptions;

/**
 * Exception levée pour signale un manque de droit, un flux coupé en cours de routez, une erreur ... <br>
 */

public class SonErreurDiverse
    extends SonException
{
  /**
   * Construit l'exception
   * @param e Exception générée à la construction du son
   */
  public SonErreurDiverse(Exception e)
  {
    super("Une erreur s'est produite lors de l'analyse du son : " +
          e.getMessage());
  }
}