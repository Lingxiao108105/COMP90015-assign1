package edu.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * from https://gist.github.com/eckig/30abf0d7d51b7756c2e7
 * @param <S>
 * @param <T>
 */
public class TextAreaTableCell<S, T> extends TableCell<S, T> {

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
        return forTableColumn(new DefaultStringConverter());
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final StringConverter<T> converter) {
        return list -> new TextAreaTableCell<>(converter);
    }

    private static <T> String getItemText(Cell<T> cell, StringConverter<T> converter) {
        return converter == null ? cell.getItem() == null ? "" : cell.getItem()
                .toString() : converter.toString(cell.getItem());
    }

    private static <T> TextArea createTextArea(final Cell<T> cell, final StringConverter<T> converter) {
        TextArea textArea = new TextArea(getItemText(cell, converter));
        textArea.setWrapText(true);

        textArea.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                cell.cancelEdit();
                t.consume();
            }
            else if(t.getCode() == KeyCode.ENTER && t.isShiftDown()) {
                t.consume();
                textArea.insertText(textArea.getCaretPosition(), "\n");
            }
            else if(t.getCode() == KeyCode.ENTER) {
                t.consume();
                if (converter == null) {
                    throw new IllegalStateException(
                            "Attempting to convert text input into Object, but provided "
                                    + "StringConverter is null. Be sure to set a StringConverter "
                                    + "in your cell factory.");
                }
                cell.commitEdit(converter.fromString(textArea.getText()));
                t.consume();
            }
        });
        return textArea;
    }

    private void startEdit(final Cell<T> cell, final StringConverter<T> converter) {
        textArea.setText(getItemText(cell, converter));

        cell.setText(null);
        cell.setGraphic(textArea);

        textArea.selectAll();
        textArea.requestFocus();
    }

    private <T> void cancelEdit(Cell<T> cell, final StringConverter<T> converter) {
        //adopt from https://stackoverflow.com/questions/22732013/javafx-tablecolumn-text-wrapping
        Text text = new Text(getItemText(cell, converter));
        text.setStyle("-fx-text-alignment:justify;");
        text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));

        cell.setText(null);
        cell.setGraphic(text);
    }

    private void updateItem(final Cell<T> cell, final StringConverter<T> converter) {

        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);

        } else {
            if (cell.isEditing()) {
                if (textArea != null) {
                    textArea.setText(getItemText(cell, converter));
                }
                cell.setText(null);
                cell.setGraphic(textArea);
            } else {
                    //adopt from https://stackoverflow.com/questions/22732013/javafx-tablecolumn-text-wrapping
                    Text text = new Text(getItemText(cell, converter));
                    text.setStyle("-fx-text-alignment:justify;");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                    cell.setText(null);
                    cell.setGraphic(text);
                }

        }

    }

    private TextArea textArea;
    private ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<>(this, "converter");

    public TextAreaTableCell() {
        this(null);
    }

    public TextAreaTableCell(StringConverter<T> converter) {
        this.getStyleClass().add("text-area-table-cell");
        setConverter(converter);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    public final void setConverter(StringConverter<T> value) {
        converterProperty().set(value);
    }

    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }

        super.startEdit();

        if (isEditing()) {
            if (textArea == null) {
                textArea = createTextArea(this, getConverter());
            }

            startEdit(this, getConverter());
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        cancelEdit(this, getConverter());
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        updateItem(this, getConverter());
    }

}
