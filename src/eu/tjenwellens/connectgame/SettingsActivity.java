package eu.tjenwellens.connectgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public void doneClickHandler(View view)
    {
        Intent intent = new Intent(this, eu.tjenwellens.connectgame.GameActivity.class);
        try
        {
            EditText rowsET = (EditText) findViewById(R.id.rowsEditText);
            EditText colsET = (EditText) findViewById(R.id.colsEditText);
            EditText connectET = (EditText) findViewById(R.id.connectEditText);
            CheckBox gravityCB = (CheckBox) findViewById(R.id.gravityCheckBox);
            CheckBox encloseCB = (CheckBox) findViewById(R.id.encloseCheckBox);
            CheckBox rotationGravityCB = (CheckBox) findViewById(R.id.rotationGravityCheckBox);
            int rows = Integer.parseInt(rowsET.getText().toString());
            int cols = Integer.parseInt(colsET.getText().toString());
            int connect = Integer.parseInt(connectET.getText().toString());
            boolean gravity = gravityCB.isChecked();
            boolean enclose = encloseCB.isChecked();
            boolean rotationGravity = rotationGravityCB.isChecked();
            intent.putExtra("rows", rows);
            intent.putExtra("cols", cols);
            intent.putExtra("connect", connect);
            intent.putExtra("gravity", gravity);
            intent.putExtra("enclose", enclose);
            intent.putExtra("rotationGravity", rotationGravity);
        } catch (NumberFormatException e)
        {
        }
        startActivity(intent);
        finish();
    }
}
