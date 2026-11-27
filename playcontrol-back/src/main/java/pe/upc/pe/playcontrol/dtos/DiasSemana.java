package pe.upc.pe.playcontrol.dtos;

/**
 * Formato comun para listas de dias: separadas por comas, sin tildes ni espacios.
 * Ejemplo: LUN,MAR,MIE,JUE,VIE
 */
public final class DiasSemana {
    public static final String DIA = "(LUN|MAR|MIE|JUE|VIE|SAB|DOM)";
    public static final String REGEX = DIA + "(," + DIA + ")*";
    public static final String MENSAJE = "Debe ser una lista separada por comas de LUN,MAR,MIE,JUE,VIE,SAB,DOM";

    private DiasSemana() {
    }
}
