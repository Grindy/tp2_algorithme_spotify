package com.maisonneuve.tp2_algorithme_spotify.utils;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextField;
import java.util.function.UnaryOperator;

// Fonction générée par Gemini
public final class FormaterFieldDureeMax {

    private FormaterFieldDureeMax() {}

    public static void appliquerFormatDuree(TextField field){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            // Autorise la réinitialisation directe via setText("00:00") ou toute valeur valide complète
            if (change.getText().matches("^[0-9]{2}:[0-5][0-9]$")) {
                return change;
            }
            // Récupère le texte actuel ou "00:00" s'il est vide/incomplet
            String currentText = change.getControlText();
            if (currentText.length() != 5) {
                currentText = "00:00";
            }

            // 1. Touche Backspace / Delete
            if (change.getText().isEmpty()) {
                int start = change.getRangeStart();
                int end = change.getRangeEnd();

                if (start != end) {
                    char[] chars = currentText.toCharArray();
                    for (int i = start; i < end; i++) {
                        if (i != 2) {
                            chars[i] = '0';
                        }
                    }
                    change.setRange(0, change.getControlText().length());
                    change.setText(new String(chars));
                    change.setCaretPosition(start);
                    change.setAnchor(start);
                    return change;
                }
                return change;
            }

            // 2. Frappe d'un chiffre
            if (change.getText().matches("[0-9]")) {
                int pos = change.getRangeStart();

                // Si le curseur est sur le ':', on passe directement au chiffre des secondes
                if (pos == 2) {
                    pos = 3;
                }

                // Bloque si le curseur est au-delà du 5e caractère
                if (pos >= 5) {
                    return null;
                }

                char digit = change.getText().charAt(0);

                // Validation des dizaines de secondes (index 3 : max 59 secondes)
                if (pos == 3 && digit > '5') {
                    return null;
                }

                char[] chars = currentText.toCharArray();
                chars[pos] = digit;

                int nextCaret = (pos + 1 == 2) ? 3 : pos + 1;

                change.setRange(0, change.getControlText().length());
                change.setText(new String(chars));
                change.setCaretPosition(nextCaret);
                change.setAnchor(nextCaret);
                return change;
            }

            // Rejette toute autre touche non numérique
            return null;
        };

        // Initialise le TextFormatter avec "00:00" par défaut
        field.setTextFormatter(new TextFormatter<>(filter));
        field.setText("00:00");
    }
}
