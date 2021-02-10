package com.nice.tools;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class Tool_tochinanum_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private Toolbar tochinanum_toolbar;
    private TextView tochinanum_chinatxt;
    private TextInputLayout tochinanum_edit;
    private MaterialButton tochinanum_del;
    private MaterialButton tochinanum_cpoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_tochinanum);

        intview();
        ingdata();
        //基础代码
        //Log.d(TAG, String.valueOf(mode.toChinese("3211311112303999.321")));
    }

    private void ingdata() {
        //监听编辑框
        tochinanum_edit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")) {
                    String newdata = mode.toChinese(String.valueOf(charSequence));
                    tochinanum_chinatxt.setText(newdata);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //清空
        tochinanum_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tochinanum_edit.getEditText().setText("");
                tochinanum_chinatxt.setText("零");
            }
        });
        //复制
        tochinanum_cpoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chinadata = tochinanum_chinatxt.getText().toString();
                if (chinadata != null) {
                    mode.copy(view.getContext(), chinadata);
                }
            }
        });

    }

    private void intview() {
        tochinanum_toolbar = findViewById(R.id.tochinanum_toolbar);
        tochinanum_chinatxt = findViewById(R.id.tochinanum_chinatxt);
        tochinanum_edit = findViewById(R.id.tochinanum_edit);
        tochinanum_del = findViewById(R.id.tochinanum_del);
        tochinanum_cpoy = findViewById(R.id.tochinanum_cpoy);

        setSupportActionBar(tochinanum_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //获取ID，判断id，设置事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //返回键事件
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
