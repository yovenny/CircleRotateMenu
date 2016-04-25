##CircleRotateMenuView
***

  Android 圆形菜单集，支持收起和展开，旋转。

##功能点

    - 拖动旋转柄可以旋转,支持最大最小旋转角度.
    - 点击菜单柄展开和收取菜单集.
    - 动态分配菜单集位置,菜单集个数任意,起始菜单柄位置自定义,旋转柄起始位置自定义

##Issue
***
    *运用简单的内部添加控件,控制动画,控制touchEvent实现.辅助控件较多可能存在性能的问题.
    *由于rotateAnimation.RELATIVE_TO_PARENT不能使菜单项围绕中心点旋转,
    现使用折衷的方式(rotateAnimation.RELATIVE_TO_SELF在菜单项套一层view,但相应的代码会增多)
    *暂未调查其他相似控件的实现原理，有更好的实现方式将会引入优化。

##Usage
    
###    ＊incode＊
      
      `
      private View.OnClickListener[] sClickArray = new View.OnClickListener[]{
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          cvMainCircle.toggleMenu();
                      }
                  },
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(MainActivity.this, "click2", Toast.LENGTH_SHORT).show();
                      }
                  }
                  ,
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(MainActivity.this, "click3", Toast.LENGTH_SHORT).show();
                      }
                  }
                  ,
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(MainActivity.this, "click4", Toast.LENGTH_SHORT).show();
                      }
                  }
                  ,
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(MainActivity.this, "click5", Toast.LENGTH_SHORT).show();
                      }
                  }
                  ,
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(MainActivity.this, "click6", Toast.LENGTH_SHORT).show();
                      }
                  }
      
          };
      
      
          private static int[] sBtnArray = new int[]{R.drawable.btn_more_selector
                  , R.drawable.btn_graph_selector
                  , R.drawable.btn_timer_selector
                  , R.drawable.btn_power_selector
                  , R.drawable.btn_hot_selector
                  ,R.drawable.btn_cold_selector
          };
      
      //rotate handle
      TextView tv = new TextView(MainActivity.this);
              tv.setText("999");
              tv.setTextSize(18);
              tv.setTextColor(Color.WHITE);
      
      
      
              cvMainCircle = (CircleRotateView) findViewById(R.id.cv_main_circle);
              cvMainCircle.setClickable(true);
              cvMainCircle.setFocusable(true);
              cvMainCircle.setMenuResource(sBtnArray)
                      .setMenuClickListener(sClickArray)
                      .setCircleHandleView(tv)
                      //min write
                      .setCircleBgResource(R.drawable.main_circle)
                      .setMenuHandleStartDegree(90)
                      .setMenuIntervalDegree(30)
                      .setRotateHandleStartDegree(-43)
                      .setMenuItemWidth(120)
      
                      .setRotateDegree(180,-180)
                      .setOnRotateListener(new CircleRotateView.OnRotateListener() {
                          @Override
                          public void onRotateMin() {
                              Toast.makeText(MainActivity.this, "min", Toast.LENGTH_SHORT).show();
                          }
      
                          @Override
                          public void onRotateMax() {
                              Toast.makeText(MainActivity.this, "max", Toast.LENGTH_SHORT).show();
                          }
      
                          @Override
                          public void onRotate(float degree) {
      
                          }
                      });
                      
    `
                      
###  ＊in xml＊
      ` 
        <com.yovenny.circlerotatemenu.CircleRotateView
               android:id="@+id/cv_main_circle"
               android:layout_width="300dp"
               android:layout_height="300dp"
               android:layout_centerInParent="true"
               app:circle_handle_start_degree="-45"
               app:menu_handle_start_degree="90"
               app:menu_interval_degree="30"
               app:menu_width="60dp"
               app:max_rotate_degree="180"
               app:min_rotate_degree="-180"
               app:circle_bg="@drawable/main_circle"/>
       
      `

