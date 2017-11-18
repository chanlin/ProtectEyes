package com.huayinghealth.protecteyes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
 
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContex;
    private Fragment[] fragments;
    private RadioGroup bottomRg;
    private LinearLayout ll_one,ll_two,ll_three,ll_four,ll_five,ll_six,ll_seven,ll_eight;
    private Switch switch_one,switch_two,switch_three,switch_four,switch_five,switch_six,switch_seven,switch_eight;
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
        ll_one = (LinearLayout) findViewById(R.id.ll_left_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_left_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_left_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_left_four);
        ll_five = (LinearLayout) findViewById(R.id.ll_left_five);
        ll_six = (LinearLayout) findViewById(R.id.ll_left_six);
        ll_seven = (LinearLayout) findViewById(R.id.ll_left_seven);
        ll_eight = (LinearLayout) findViewById(R.id.ll_left_eight);

        switch_one = (Switch) findViewById(R.id.switch_one);
        switch_two = (Switch) findViewById(R.id.switch_two);
        switch_three = (Switch) findViewById(R.id.switch_three);
        switch_four = (Switch) findViewById(R.id.switch_four);
        switch_five = (Switch) findViewById(R.id.switch_five);
        switch_six = (Switch) findViewById(R.id.switch_six);
        switch_seven = (Switch) findViewById(R.id.switch_seven);
        switch_eight = (Switch) findViewById(R.id.switch_eight);

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

        ll_one .setOnClickListener(this);
        ll_two.setOnClickListener(this);
        ll_three.setOnClickListener(this);
        ll_four .setOnClickListener(this);
        ll_five .setOnClickListener(this);
        ll_six .setOnClickListener(this);
        ll_seven .setOnClickListener(this);
        ll_eight .setOnClickListener(this);

        switch_one .setOnClickListener(this);
        switch_two.setOnClickListener(this);
        switch_three.setOnClickListener(this);
        switch_four .setOnClickListener(this);
        switch_five .setOnClickListener(this);
        switch_six .setOnClickListener(this);
        switch_seven .setOnClickListener(this);
        switch_eight .setOnClickListener(this);
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
            case R.id.switch_one:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;
            case R.id.switch_two:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;
            case R.id.switch_three:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;
            case R.id.switch_four:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;
            case R.id.switch_five:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;
            case R.id.switch_six:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;
            case R.id.switch_seven:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;
            case R.id.switch_eight:
                Toast.makeText(mContex,"one", Toast.LENGTH_LONG).show();
                break;


            case R.id.ll_left_one:
                fragmentTransaction.show(fragments[0]).commit();
                break;
            case R.id.ll_left_two:
                fragmentTransaction.show(fragments[1]).commit();
                break;
            case R.id.ll_left_three:
                fragmentTransaction.show(fragments[2]).commit();
                break;
            case R.id.ll_left_four:
                fragmentTransaction.show(fragments[3]).commit();
                break;
            case R.id.ll_left_five:
                fragmentTransaction.show(fragments[4]).commit();
                break;
            case R.id.ll_left_six:
                fragmentTransaction.show(fragments[5]).commit();
                break;
            case R.id.ll_left_seven:
                fragmentTransaction.show(fragments[6]).commit();
                break;
            case R.id.ll_left_eight:
                fragmentTransaction.show(fragments[7]).commit();
                break;
        }
    }
}
