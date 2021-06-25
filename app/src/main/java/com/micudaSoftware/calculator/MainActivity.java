package com.micudaSoftware.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    HistoryDatabase historyDatabase;
    HorizontalScrollView hScroll;
    EditText editText;
    boolean calculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hScroll = findViewById(R.id.horizontalScroll);
        editText = findViewById(R.id.editTextNumber);
        historyDatabase = new HistoryDatabase(this);
        loadHistory(historyDatabase);
    }

    @Override
    protected void onPause() {
        String text = editText.getText().toString();
        if (text.contains("="))
            writeHistory(text);
            saveHistory(historyDatabase, text);
            editText.setText(null);
        super.onPause();
    }

    public void write(View view) {
        String text = editText.getText().toString();
        if (calculated) {
            writeHistory(text);
            saveHistory(historyDatabase, text);
            calculated = false;
        }
        if (view.getId() != R.id.plus
                && view.getId() != R.id.minus
                && view.getId() != R.id.multiple
                && view.getId() != R.id.division
                && view.getId() != R.id.backspace
                && view.getId() != R.id.clear)
            text = clear(text);
        switch (view.getId()){
            case R.id.one:
                editText.setText(text + "1");
                break;
            case R.id.two:
                editText.setText(text + "2");
                break;
            case R.id.three:
                editText.setText(text + "3");
                break;
            case R.id.four:
                editText.setText(text + "4");
                break;
            case R.id.five:
                editText.setText(text + "5");
                break;
            case R.id.six:
                editText.setText(text + "6");
                break;
            case R.id.seven:
                editText.setText(text + "7");
                break;
            case R.id.eight:
                editText.setText(text + "8");
                break;
            case R.id.nine:
                editText.setText(text + "9");
                break;
            case R.id.zero:
                editText.setText(text + "0");
                break;
            case R.id.point:
                if (text.equals(""))
                    text = "0";
                editText.setText(text + ".");
                break;
            case R.id.plus:
                editText.setText(equals(text) + "+");
                break;
            case R.id.minus:
                editText.setText(equals(text) + "-");
                break;
            case R.id.multiple:
                editText.setText(equals(text) + "*");
                break;
            case R.id.division:
                editText.setText(equals(text) + "/");
                break;
            case R.id.clear:
                editText.setText("");
                break;
            case R.id.backspace:
                editText.setText(text.substring(0, text.length() - 1));
                break;
        }
        hScroll.post(() -> hScroll.fullScroll(View.FOCUS_RIGHT));
    }

    private String equals(String text) {
        if (text.contains("=")) {
            return text.split("=")[1];
        } else
            return text;
    }

    private String clear(String text) {
        if (text.contains("=")) {
            text = "";
        }
        return text;
    }

    public void calculate(View view){
        try {
            String text = editText.getText().toString();
            boolean error = false;
            if (!text.contains("=")) {
                String[] splitNumbers = text.split("[\\-+*/]");
                ArrayList<Float> numbers = new ArrayList<>();
                for (String s : splitNumbers)
                    numbers.add(Float.parseFloat(s));

                String[] splitOperators = text.split("[0-9]+\\.*[0-9]*");
                ArrayList<Character> operators = new ArrayList<>();
                for (int i = 1; i < splitOperators.length; i++)
                    operators.add(splitOperators[i].charAt(0));

                while (operators.contains('*') || operators.contains('/')) {
                    if (operators.contains('*')) {
                        float multiply;
                        multiply = numbers.get(operators.indexOf('*')) * numbers.get(operators.indexOf('*') + 1);
                        numbers.remove(operators.indexOf('*'));
                        numbers.remove(operators.indexOf('*'));
                        numbers.add(operators.indexOf('*'), multiply);
                        operators.remove(Character.valueOf('*'));
                    } else if (operators.contains('/')) {
                        float division;
                        if (numbers.get(operators.indexOf('/') + 1) != 0) {
                            division = numbers.get(operators.indexOf('/')) / numbers.get(operators.indexOf('/') + 1);
                            numbers.remove(operators.indexOf('/'));
                            numbers.remove(operators.indexOf('/'));
                            numbers.add(operators.indexOf('/'), division);
                            operators.remove(Character.valueOf('/'));
                        } else {
                            Toast.makeText(this, "Math error", Toast.LENGTH_SHORT).show();
                            error = true;
                            break;
                        }
                    }
                }

                if (!error) {
                    float equals = 0;
                    for (int i = 0; i < numbers.size(); i++) {
                        if (i == 0)
                            equals = numbers.get(0);
                        else {
                            switch (operators.get(i - 1)) {
                                case '+':
                                    equals = equals + numbers.get(i);
                                    break;
                                case '-':
                                    equals = equals - numbers.get(i);
                                    break;
                            }
                        }
                    }
                    editText.setText(editText.getText() + " =" + format(equals));
                    calculated = true;
                    hScroll.post(() -> hScroll.fullScroll(View.FOCUS_RIGHT));
                }
            }
        } catch (IndexOutOfBoundsException e){
            Toast.makeText(this, "Syntax Error", Toast.LENGTH_SHORT).show();
        } catch (ArithmeticException e) {
            Toast.makeText(this, "Math error", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Math error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static String format(float d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    public void writeHistory(String history) {
        TextView textView = findViewById(R.id.history);
        ScrollView scrollView = findViewById(R.id.scroll);
        if (textView.getText().toString() == "")
            textView.setText(textView.getText() + history);
        else
            textView.setText(textView.getText() + "\n" + history);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void loadHistory(HistoryDatabase db) {
        ArrayList<String> maths = db.getMaths();
        ArrayList<Float> results = db.getResults();
        for (int i=0; i < maths.size(); i++) {
            writeHistory(maths.get(i) + " =" + format(results.get(i)));
        }
    }

    private void saveHistory(HistoryDatabase db, String text) {
        String math = text.split(" =")[0];
        Float result = Float.parseFloat(text.split(" =")[1]);
        db.insertData(math, result);

    }
}
