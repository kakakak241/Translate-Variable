package com.danila.zubov.someplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
        public void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
            Document document = editor.getDocument();
            if (!document.isWritable()) {
                return;
            }

            //String token = "t1.9euelZqMnpaRx5THys_Lyc_KkpOZie3rnpWaz5OWyc3HjZ3KkpjJisqPzY3l8_dtbD5j-e8ufW9N_t3z9y0bPGP57y59b03-.K_-Ko_HhU8r8bMBAlUpIJFtJRIGtBP9XsOLEHLp8390mCU3cW8fVWcPhFkhH-IIZlktkDFdoBlOWeZs_lgs5Bw";


            caret = editor.getCaretModel().getCurrentCaret();
            String variable = caret.getSelectedText();

            CloseableHttpClient client = HttpClients.createDefault();
            String url = String.format("https://translate.api.cloud.yandex.net/translate/v2/translate?targetLanguageCode=en&texts=%s&folderId=b1g9jakgelm2mijkusoe", variable);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", "Bearer t1.9euelZqMnpaRx5THys_Lyc_KkpOZie3rnpWaz5OWyc3HjZ3KkpjJisqPzY3l8_dtbD5j-e8ufW9N_t3z9y0bPGP57y59b03-.K_-Ko_HhU8r8bMBAlUpIJFtJRIGtBP9XsOLEHLp8390mCU3cW8fVWcPhFkhH-IIZlktkDFdoBlOWeZs_lgs5Bw");

            CloseableHttpResponse response = null;
            try {
                response = client.execute(httpPost);
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (variable != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                Translations result = null;
                try {
                    result = objectMapper.readValue(EntityUtils.toString(response.getEntity(), "UTF-8"), Translations.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                document.replaceString(caret.getSelectionStart(), caret.getSelectionEnd(),result.getTranslations().get(0).getText());
            }
        }

    }
}