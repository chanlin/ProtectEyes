package com.huayinghealth.protecteyes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContex;
    private Fragment[] fragments;
    private RadioGroup bottomRg;
    private LinearLayout ll_one,ll_two,ll_three,ll_four,ll_five,ll_six,ll_seven,ll_eight;
    private Switch switch_one,switch_two,switch_three,switch_four,switch_five,switch_six,switch_seven,switch_eight;
    private RadioButton rbOne, rbTwo, rbThree, rbFour, rbFive, rbSix, rbSenve, rbEight;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ImageView bt_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContex = this;
        init();
       // setClick();
        Intent service = new Intent(mContex, LocationService.class);
        mContex.startService(service);
    }

    private  void init(){
        fragments = new Fragment[8];
        fragmentManager = getFragmentManager();
        fragments[0] = fragmentManager.findFragmentById(R.id.fragement_one);
        fragments[1] = fragmentManager.findFragmentById(R.id.fragement_two);
        fragments[2] = fragmentManager.findFragmentById(R.id.fragement_three);
        fragments[3] = fragmentManager.findFragmentById(R.id.fragement_four);
        fragments[4] = fragmentManager.findFragmentById(R.id.fragement_five);
        fragments[5] = fragmentManager.findFragmentById(R.id.fragement_six);
        fragments[6] = fragmentManager.findFragmentById(R.id.fragement_seven);
        fragments[7] = fragmentManager.findFragmentById(R.id.fragement_eight);

        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(fragments[0])
                .hide(fragments[1])
                .hide(fragments[2])
                .hide(fragments[3])
                .hide(fragments[4])
                .hide(fragments[5])
                .hide(fragments[6])
                .hide(fragments[7]);
        fragmentTransaction.show(fragments[0]).commit();
        bt_exit = (ImageView) findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(this);

       // bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
        rbOne = (RadioButton) findViewById(R.id.rbOne);
        rbTwo = (RadioButton) findViewById(R.id.rbTwo);
        rbThree = (RadioButton) findViewById(R.id.rbThree);
        rbFour = (RadioButton) findViewById(R.id.rbFour);
        rbFive = (RadioButton) findViewById(R.id.rbFive);
        rbSix = (RadioButton) findViewById(R.id.rbSix);
        rbSenve = (RadioButton) findViewById(R.id.rbSenve);
        rbEight = (RadioButton) findViewById(R.id.rbEight);

//        switch_one = (Switch) findViewById(R.id.switch_one);
//        switch_two = (Switch) findViewById(R.id.switch_two);
//        switch_three = (Switch) findViewById(R.id.switch_three);
//        switch_four = (Switch) findViewById(R.id.switch_four);
//        switch_five = (Switch) findViewById(R.id.switch_five);
//        switch_six = (Switch) findViewById(R.id.switch_six);
//        switch_seven = (Switch) findViewById(R.id.switch_seven);
//        switch_eight = (Switch) findViewById(R.id.switch_eight);

        setClick();
    }

    private void setClick(){
        /*bottomRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (fragmentTransaction == null) {
                    fragmentTransaction = fragmentManager.beginTransaction()
                            .hide(fragments[0])
                            .hide(fragments[1])
                            .hide(fragments[2])
                            .hide(fragments[3])
                            .hide(fragments[4])
                            .hide(fragments[5])
                            .hide(fragments[6])
                            .hide(fragments[7]);
                }
                switch (checkedId){


                }
            }
        });*/

        rbOne .setOnClickListener(this);
        rbTwo.setOnClickListener(this);
        rbThree.setOnClickListener(this);
        rbFour .setOnClickListener(this);
        rbFive .setOnClickListener(this);
        rbSix .setOnClickListener(this);
        rbSenve .setOnClickListener(this);
        rbEight .setOnClickListener(this);

//        switch_one .setOnClickListener(this);
//        switch_two.setOnClickListener(this);
//        switch_three.setOnClickListener(this);
//        switch_four .setOnClickListener(this);
//        switch_five .setOnClickListener(this);
//        switch_six .setOnClickListener(this);
//        switch_seven .setOnClickListener(this);
//        switch_eight .setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(fragments[0])
                .hide(fragments[1])
                .hide(fragments[2])
                .hide(fragments[3])
                .hide(fragments[4])
                .hide(fragments[5])
                .hide(fragments[6])
                .hide(fragments[7]);
        switch (view.getId()){
            case R.id.bt_exit:
                finish();
                break;
//            case R.id.switch_one:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.switch_two:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.switch_three:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.switch_four:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.switch_five:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.switch_six:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.switch_seven:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.switch_eight:
//                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
//                break;


            case R.id.rbOne:
                fragmentTransaction.show(fragments[0]).commit();
                break;
            case R.id.rbTwo:
                fragmentTransaction.show(fragments[1]).commit();
                break;
            case R.id.rbThree:
                fragmentTransaction.show(fragments[2]).commit();
                break;
            case R.id.rbFour:
                fragmentTransaction.show(fragments[3]).commit();
                break;
            case R.id.rbFive:
                fragmentTransaction.show(fragments[4]).commit();
                break;
            case R.id.rbSix:
                fragmentTransaction.show(fragments[5]).commit();
                break;
            case R.id.rbSenve:
                fragmentTransaction.show(fragments[6]).commit();
                break;
            case R.id.rbEight:
                fragmentTransaction.show(fragments[7]).commit();
                break;
        }
    }
}
