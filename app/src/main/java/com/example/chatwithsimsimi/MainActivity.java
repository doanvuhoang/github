package com.example.chatwithsimsimi;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chatwithsimsimi.Helper.CustomAdapter;
import com.example.chatwithsimsimi.Helper.HttpDataHandler;
import com.example.chatwithsimsimi.Model.ChatModel;
import com.example.chatwithsimsimi.Model.SimsimiModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    List<ChatModel> list_chat = new ArrayList<>();
    FloatingActionButton btn_send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_of_message);
        editText = findViewById(R.id.user_message);
        btn_send_message = findViewById(R.id.fab);

        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                ChatModel model = new ChatModel(text,true);
                list_chat.add(model);
                new SimsimiAPI().execute(list_chat);
                editText.setText("");

            }
        });
    }

    private class SimsimiAPI extends AsyncTask<List<ChatModel>,Void,String>{
        String stream = null;
        List<ChatModel> models;
        String text = editText.getText().toString();
        @Override
        protected String doInBackground(List<ChatModel>... lists) {
            String url = String.format("http://sandbox.api.simsimi.com/request.p?key=%s&lc=en&ft=1.0&text=%s",getString(R.string.Simsimi_API),text);
            models = lists[0];
            HttpDataHandler httpDataHandler = new HttpDataHandler();
            stream = httpDataHandler.GetHttpData(url);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            SimsimiModel response = gson.fromJson(s, SimsimiModel.class);
            ChatModel chatModel = new ChatModel(response.getResponse(), false);
            models.add(chatModel);
            CustomAdapter adapter = new CustomAdapter(models, getApplicationContext());
            listView.setAdapter(adapter);
            super.onPostExecute(s);
        }
    }
}
