# Pickture
A small-scale android  widget which provides easy attach to photo selection.
<br>q：一个怎样的控件呢？a:照片的选择+照片Gridview展示（拖拽排序、删除）+照片viewpager演示（放大、缩小）
</br>
## Intro
* 场景：社交类、工具类、游戏类App
* 进度：由于目前比较忙，viewpager的演示，放大缩小功能尚未完成
* 用途：照片的选择、演示
* 如何使用：目前还在处理Maven仓库的问题，暂时无法提供`Library Denpendency`

##如何使用呢？
~Scene1. 只用照片选择
调用下面的代码即可进入到照片选择
``` java
Pickture.with(MainActivity.this).column(COLUMN).max(MAX).selected(selectedList).create();
```
column：照片显示的行
max：选择的照片数量
selected：设置已选择的照片
``` java
ArrayList<String> selectedList = new ArrayList<>();
``` 
在 onActivityResult接收你选择的照片
``` java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            selectedList = data.getStringArrayListExtra(Pickture.PARAM_PICKRESULT);

            mPickRecyclerView.bind(selectedList);
        }
    }
``` 
~Scene2. 使用照片展示的GridView
这个时候需要使用到 
``` java
import me.stefan.pickturelib.widget.PickRecyclerView;
``` 
具体使用方式为:
``` java
        mPickRecyclerView = (PickRecyclerView) findViewById(R.id.__prv);

        findViewById(R.id.__get_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启用图片选择功能
                mPickture.selected(selectedList).create();
                //如果只是使用读取照片功能，可使用下面这个快捷通道，不需要再调用showOn方法去同步参数了

            }
        });

         mPickture = Pickture.with(MainActivity.this).column(COLUMN).max(MAX).hasCamera(true).selected(selectedList);

        //当需要同步展示到 PickRecyclerView 需要同步基础参数给你的 mPickRecyclerView ，这个方法就是用于同步的
        mPickture.showOn(mPickRecyclerView);

        mPickRecyclerView.setOnOperateListener(new OperateListenerAdapter() {

            @Override
            public void onClickAdd() {
                //点击添加按钮
                mPickture.selected(selectedList).create();
            }
        });
``` 
xml中的文件就是正常的控件使用方式，就不贴出啦



## 效果图
 ![Pickture](/gif/howtouse.gif)  
 
### [APK下载](https://github.com/JmStefanAndroid/Pickture/tree/master/apk/pickture.apk?raw=true)  
 
### THE END
 * 项目里面的一些实现借鉴了`github`上的一些图片选择库，非常感谢他们共享了这些资源
 * 如果有技术合作欢迎联系我
 * 如果有bug，麻烦您及时反馈，我也将在第一时间进行处理
 * 如何联系我：648701906@qq.com
 * 有任何问题可以留言给我，一个人的力量是有限的，有你们的支持才能让它更加useful，提前谢谢你们的star~
 <br></br>
 
License
--------
```
Copyright (C) 2016 JmStefanAndroid

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
