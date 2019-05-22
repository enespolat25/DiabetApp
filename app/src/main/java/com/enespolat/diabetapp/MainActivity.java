package com.enespolat.diabetapp;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    private Interpreter tflite;
    String modelFile= "converted_model.tflite";
    EditText et1,et2,et3,et4,et5,et6,et7,et8;
    Button btn,btn2;
    TextView tv1;
    Float input_veriler, n1,n2,n3,n4,n5,n6,n7,n8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=findViewById(R.id.editText2);
        et2=findViewById(R.id.editText3);
        et3=findViewById(R.id.editText4);
        et4=findViewById(R.id.editText5);
        et5=findViewById(R.id.editText6);
        et6=findViewById(R.id.editText7);
        et7=findViewById(R.id.editText8);
        et8=findViewById(R.id.editText9);
        tv1=findViewById(R.id.tv1);
        btn=findViewById(R.id.button);
        btn2=findViewById(R.id.button2);




        try {
            tflite = new Interpreter(loadModelFile(this, modelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                n1=Float.parseFloat(et1.getText().toString());
                n2=Float.parseFloat(et2.getText().toString());
                n3=Float.parseFloat(et3.getText().toString());
                n4=Float.parseFloat(et4.getText().toString());
                n5=Float.parseFloat(et5.getText().toString());
                n6=Float.parseFloat(et6.getText().toString());
                n7=Float.parseFloat(et7.getText().toString());
                n8=Float.parseFloat(et8.getText().toString());


                //float n1 = 1;
                //float n2 = 89;
                //float n3 = 66;
                //float n4 = 23;
                //float n5 = 94;
                //float n6 = 28.1f;
                //float n7 = 0.167f;
                //float n8 = 21;
                float[][] inp = new float[][]{{n1, n2, n3, n4, n5, n6, n7, n8}};

                float[][] out = new float[][]{{0}};
                tflite.run(inp, out);
                Integer a=Math.round(out[0][0]);
                String ilave_metin;
                if (a<0.5)
                {
                    tv1.setBackgroundColor(Color.GREEN);
                    ilave_metin=" Sonuç Negatif:)";
                }else
                {
                    tv1.setBackgroundColor(Color.RED);
                    ilave_metin=" Sonuç Pozitif:(";

                }
                String sonuc = String.valueOf(Math.round(out[0][0]));
                Log.d("Sonuç", sonuc);
                tv1.setText(sonuc+ilave_metin);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                et6.setText("");
                et7.setText("");
                et8.setText("");
                tv1.setBackgroundColor(Color.LTGRAY);
                tv1.setText("Sonuç Burada Görülecektir.");
            }
        });


    }
    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
