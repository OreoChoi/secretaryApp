package com.example.junho.secretaryapps.calculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.junho.secretaryapps.R;


public class TouchCalculatorActivity extends AppCompatActivity {
    private static final String ONE = "1", TWO = "2", THREE = "3", FOUR = "4",
            FIVE = "5", SIX = "6", SEVEN = "7", EIGHT = "8", NINE = "9", DOUBLE_ZERO = "00",
            ZERO = "0";
    ImageButton calResult, calPeriod, calDoubleZero, calZero, calCloseBracket,
            calPlus, calThree, calTwo, calOne, calOpenBracket, calSubtract,
            calSix, calFive, calFour, calPlusMinus, calMultiply, calNine, calEight,
            calSeven, calPercent, calDivision, calBack, calClear;
    TextView calModifyTxtView;
    EditText inputEditText;
    TouchCalculator touchCalculator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touchcalculator);

        calResult = (ImageButton) findViewById(R.id.calResult);
        calPeriod = (ImageButton) findViewById(R.id.calPeriod);
        calDoubleZero = (ImageButton) findViewById(R.id.calDoubleZero);
        calZero = (ImageButton) findViewById(R.id.calZero);
        calCloseBracket = (ImageButton) findViewById(R.id.calCloseBracket);
        calPlus = (ImageButton) findViewById(R.id.calPlus);
        calThree = (ImageButton) findViewById(R.id.calThree);
        calTwo = (ImageButton) findViewById(R.id.calTwo);
        calOne = (ImageButton) findViewById(R.id.calOne);
        calOpenBracket = (ImageButton) findViewById(R.id.calOpenBracket);
        calSubtract = (ImageButton) findViewById(R.id.calSubtract);
        calSix = (ImageButton) findViewById(R.id.calSix);
        calFive = (ImageButton) findViewById(R.id.calFive);
        calFour = (ImageButton) findViewById(R.id.calFour);
        calPlusMinus = (ImageButton) findViewById(R.id.calPlusMinus);
        calMultiply = (ImageButton) findViewById(R.id.calMultiply);
        calNine = (ImageButton) findViewById(R.id.calNine);
        calEight = (ImageButton) findViewById(R.id.calEight);
        calSeven = (ImageButton) findViewById(R.id.calSeven);
        calPercent = (ImageButton) findViewById(R.id.calPercent);
        calDivision = (ImageButton) findViewById(R.id.calDivision);
        calBack = (ImageButton) findViewById(R.id.calBack);
        calClear = (ImageButton) findViewById(R.id.calClear);
        calModifyTxtView = (TextView) findViewById(R.id.calModifyTxtView);
        inputEditText = (EditText) findViewById(R.id.inputEditText);
        touchCalculator = new TouchCalculator();

        /* 계산기 숫자 버튼 ClickListener*/
        calOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + ONE);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + TWO);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + THREE);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + FOUR);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + FIVE);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + SIX);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + SEVEN);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + EIGHT);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + NINE);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calDoubleZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + DOUBLE_ZERO);
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + ZERO);
                inputEditText.setSelection(inputEditText.length());
            }
        });
        calPeriod.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + ".");
                inputEditText.setSelection(inputEditText.length());
            }
        });

        /* 계산기 Clear 버튼 ClickListener*/
        calClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchCalculator.clear();
                inputEditText.setText("");
                calModifyTxtView.setText("");
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = inputEditText.getText().toString();
                try {
                    inputEditText.setText(temp.substring(0, temp.length() - 1));
                    inputEditText.setSelection(inputEditText.length());
                } catch (StringIndexOutOfBoundsException e) {
                    inputEditText.setText("");
                }
            }
        });






        /* 계산기 기호 버튼 ClickListener */
        calPercent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String temp = inputEditText.getText().toString();
                setFactor(temp,"%");
            }
        });

        calDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = inputEditText.getText().toString();
                setFactor(temp, "÷");
            }
        });

        calPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = inputEditText.getText().toString();
                setFactor(temp, "+");
            }
        });

        calMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = inputEditText.getText().toString();
                setFactor(temp, "×");
            }
        });

        calSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = inputEditText.getText().toString();
                setFactor(temp, "-");
            }
        });

        calPlusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = inputEditText.getText().toString();

                if(temp.equals("")){
                }
                else {
                    if (temp.contains("-")) {
                        temp = temp.substring(1, temp.length());
                        inputEditText.setText(temp);
                        inputEditText.setSelection(inputEditText.length());
                    } else {
                        inputEditText.setText("-" + inputEditText.getText().toString());
                        inputEditText.setSelection(inputEditText.length());
                    }
                }
            }
        });

        calOpenBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + "(");
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calCloseBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText(inputEditText.getText().toString() + ")");
                inputEditText.setSelection(inputEditText.length());
            }
        });

        calResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String result = "";
                    String inputTemp = inputEditText.getText().toString();
                    String modifyTemp = calModifyTxtView.getText().toString();

                    setFactor(inputTemp, "");
                if (touchCalculator.factor.size() >= 2) {
                    result = touchCalculator.calculate();
                    if(inputTemp.contains(".")){
                        calModifyTxtView.setText(inputTemp + "\n" + modifyTemp);
                        inputEditText.setText(result);
                        inputEditText.setSelection(inputEditText.length());
                    }else{
                        calModifyTxtView.setText(inputTemp + "\n" + modifyTemp);
                        inputEditText.setText(result.substring(0,result.indexOf(".")));
                        inputEditText.setSelection(inputEditText.length());
                    }
                } else {
                    calModifyTxtView.setText("인자는 최소 2개가 필요합니다.");
                }
            }
        });
    }

    /*
     * 저장된 수학기호가 있는지 판별
     * true =  수학기호를 기준으로 문자열을 자르고 인자를 setting
     * false = 인자를 setting합니다
     * */
    public void setFactor(String temp, String mathe) {

        String lastWord = temp.substring(temp.length() - 1, temp.length());

        /*
         * 수학기호의 재 입력을 방지한다.
         *
         * 수학기호를 재 입력 시 if문 실행
         * 수학기호를 재 입력 시 TouchCalculate.setFactor()을 미 실행
         * 마지막 입력이 수학기호일 경우 else문을 실행
         */
        if (lastWord.equals("+") || lastWord.equals("-") || lastWord.equals("÷") || lastWord.equals("×") || lastWord.equals("%")) {
            /* 만약 lastWord가 수학기호이고 calResult를 누른다면 마지막 수학기호를 삭제한다.*/
            if(mathe.equals("")){
                touchCalculator.deleteLastMathematics();
                inputEditText.setText(temp.substring(0,temp.length()-1));
            }else {
                /* 수학기호를 재 입력 시 다시 수학기호를 배치한다. */
                if (mathe.equals("+")) {
                    temp = temp.substring(0, temp.length() - 1);
                    inputEditText.setText(temp + "+");
                } else if (mathe.equals("-")) {
                    temp = temp.substring(0, temp.length() - 1);
                    inputEditText.setText(temp + "-");
                } else if (mathe.equals("×")) {
                    temp = temp.substring(0, temp.length() - 1);
                    inputEditText.setText(temp + "×");
                } else if (mathe.equals("÷")) {
                    temp = temp.substring(0, temp.length() - 1);
                    inputEditText.setText(temp + "÷");
                } else {
                    temp = temp.substring(0, temp.length() - 1);
                    inputEditText.setText(temp + "%");
                }
            }
        } else {
            /* 수학기호가 하나도 없으면 바로 인자와 수학기호를 add 시킨다. */
            if (touchCalculator.getMathematicsSize() == 0) {
                touchCalculator.setFactor(temp);
                touchCalculator.setMathematics(mathe);
            }
            /* 수학기호가 하나라도 있다면 기호를 기준으로 마지막 인자를 잘라내어 set한다.*/
            else {
                temp = touchCalculator.cutFactor(temp);
                touchCalculator.setFactor(temp);

                /*버튼 입력이 calResult가 아닌 +,-,*,/인 경우 else문을 실행한다.*/
                if (mathe.equals("")) {

                } else {
                    touchCalculator.setMathematics(mathe);
                }

            }
            inputEditText.setText(inputEditText.getText().toString() + mathe);
        }

        inputEditText.setSelection(inputEditText.length());
    }

}
