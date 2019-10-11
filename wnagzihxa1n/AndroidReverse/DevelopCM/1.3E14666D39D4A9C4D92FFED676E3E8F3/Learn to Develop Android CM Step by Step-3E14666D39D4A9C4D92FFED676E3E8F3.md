# Learn to Develop Android CM Step by Step-3E14666D39D4A9C4D92FFED676E3E8F3

**Author:wnagzihxa1n
E-Mail:wnagzihxa1n@gmail.com**

## 0x00 前言
一直到处找别人的Android CrackMe玩，看着题目，有时候会有一些不一样的想法，所以想写一些个人理解下的Android CrackMe，喜欢的同学可以来玩
- https://github.com/toToCW/AndroidCrackMes/tree/master/1.3E14666D39D4A9C4D92FFED676E3E8F3


## 0x01 Description
第一个Android CrackMe，非常简单，起个好节奏，你要Flag，我就给你Flag
```
public class MainActivity extends AppCompatActivity {

    final String RegCode = "I want Flag";
    final String Flag = "flag{Start_4ndr0id_Crack1ng_w1th_m3}";
    private EditText et_RegCode;
    private Button bt_CheckReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_RegCode = (EditText) findViewById(R.id.RegCode);
        bt_CheckReg = (Button) findViewById(R.id.CheckReg);

        bt_CheckReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_RegCode.getText().toString().equals(RegCode)) {
                    Toast.makeText(MainActivity.this, Flag, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
```

## 0x02 小结
Have Fun:)








































