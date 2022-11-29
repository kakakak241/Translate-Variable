package com.danila.zubov.someplugin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

public class TranslateAction extends EditorAction {
    public TranslateAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public TranslateAction() {
        this(new UpHandler());
    }

    private static class UpHandler extends EditorWriteActionHandler {
        private UpHandler() {
        }

        @Override
        public void executeWriteAction(Editor editor, DataContext dataContext) {
            Document document = editor.getDocument();

            if (!document.isWritable()) {
                return;
            }

            // CaretModel used to find caret position
            CaretModel caretModel = editor.getCaretModel();
            // SelectionModel used to find selection ranges
            SelectionModel selectionModel = editor.getSelectionModel();

            String variable = selectionModel.getSelectedText();
            if (variable != null) {
                Caret currentCaret = caretModel.getCurrentCaret();
                int wordEnd = currentCaret.getSelectionEnd();

                document.replaceString(wordEnd - variable.length(), wordEnd, variable);
            }
        }
    }
}
