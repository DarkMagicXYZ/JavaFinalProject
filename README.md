# JAVA大作业——葫芦娃大战妖怪

## 环境

- JavaSE - 1.8
- maven - 3.6.0
- Junit 5
- JavaFX 2.4

## 功能

- 开始：运行即出现初始界面，场景提示：按Space开始、L选择读取文件（只有按下Space之前或者在战斗结束界面按下才有响应）、Esc退出游戏（游戏全程有效）
![](https://github.com/DarkMagicXYZ/Java-2018f-pics/blob/master/1.png)

- 1.开始战斗：双方按照既定的阵型排列保持1.5s后，开始自动随机移动和攻击（回合制），当敌对阵营生物在以自己为中心的3*3空间里，即进行攻击，双方必有一个死掉（测试时采用双方各0.5的胜率），死后在战场上留下一个坟墓，且不影响其他生物的移动（即无视碰撞体积），为了使人眼适应界面的刷新，将刷新频率设置为0.7s/frame。当战场上只有一个阵营的生物时，结束战斗，弹出结束的画面，并自动将本局的战斗过程以xml的格式保存在Record\record.xml中。
![](https://github.com/DarkMagicXYZ/Java-2018f-pics/blob/master/2.png)

- 2.战斗结束：此时可以按L读取Record下的战斗记录，既包括刚刚的战斗，也有几个之前我保存的经典战斗可以回放
![](https://github.com/DarkMagicXYZ/Java-2018f-pics/blob/master/3.png)

- 3.回放阶段：在游戏开始前或者战斗结束（包括回放结束）阶段按下L键，显示文件对话框，在Record文件下选取一个文件双击，会自动播放这次记录的内容至结束，循环至2
![](https://github.com/DarkMagicXYZ/Java-2018f-pics/blob/master/4.png)

## 参数设置

- 画面刷新频率：1.4fps
- 葫芦娃阵营对妖怪胜率：50%
- 攻击距离：横纵距离<=1
- 死亡设置：在一次攻击后双方必有一个死掉
- 角色移动方式：每回合在周围3*3合法的位置选取一个位置进行瞬移
- 战场大小：9*7（对应图片分辨率900 * 700）（这个大小为了防止最终的剩两个生物难以找到对方）
- 人物分辨率大小：100 * 100
- 回合上限：无（实际测试中，回合数10-120是绝大多数情况，虽然存在最后两个生物长时间无法相遇的情况，但多数也在120内，故没设置回合上限，如果实在不想看还是Esc吧）

## 代码结构

### creature -- package

包含抽象类Creature，以及其派生类CalabashBrother、Grandpa、Serpent、Scorpion、Minion五个类

#### Creature

![](https://github.com/DarkMagicXYZ/Java-2018f-pics/blob/master/c.png)

继承Runnable，在类内加入生物的位置信息position、图片信息image，并有isAlive表示生存状况、isWait表示此线程是否处于wait()状态，用于战斗round的判定，加入了Battlefield field成员便于线程的控制

重写了run()函数，先sleep1500ms用以等待gui初始化保证最初始的阵型可以显示出来，接下类在战斗未结束的时候重复fight和move的动作，之后执行wait()等待回合结束判定的唤醒notify();

#### CalabashBrother、Grandpa、Serpent、Scorpion、Minion

继承Creature，并重新命名name，获取图片image，其中CalabashBrother新添了String color成员（本次作业未使用）

### formation -- package

包含抽象类Formation，以及其派生类BadCampFormation、GoodCampFormation两个类和Postion类

#### Formation

抽象类，仅包含名字name和位置信息Position[] array

#### BadCampFormation、GoodCampFormation

继承Formation 规定了好人阵营和坏人阵营的位置信息

#### Position

用以表示位置信息的类

### threads -- package

包含MonitorThread类和ReplayThread

#### MonitorThread

继承Thread，承担了回合控制和gui显示的重任，在此线程启动（按下空格键会启动Battlefield类里面有相应控制），战斗结束前，循环地检查是否回合结束，如果结束即将本回合的状态记录在Battlefield的record中

#### ReplayThread

继承Thread，在每次replay的情况下会重新开始线程，利用其读取到的replaydata以回合的形式刷新每一个回合

### attribute -- package

#### Attribute

接口，用以保存常量，包含好人遇到敌人和坏人遇到敌人的胜率

### main -- package

包含Battlefield、Unit、Main、IOController、MyCanvas类

#### MyCanvas

继承Canvas类，初始化时设置了战场背景的图片，以及文字的字体和大小

#### Unit

表示战场的每一个单元，包含position和creature两个成员，其中位置在Battlefield当中unitsInit()设置，生物信息随着战场的变化而变化

#### IOController

用于输入输出处理的控制，其中record()方法用于将Battlefield中的战斗信息保存，read()用于读取保存好的xml文件

#### Main

继承Application，用于gui

#### Battlefield

![](https://github.com/DarkMagicXYZ/Java-2018f-pics/blob/master/b1.png) 

包含了战场的基本单元组Unit[][] units，
badguys、goodguys两个阵营的ArrayList，
对应生物线程threads的ArrayList、两个控制线程MonitorThread monitor、ReplayThread replay，
文件控制类IOController contr、io保存和读取的record和replaydata，
画布MyCanvas can，
以及相关的一些参数

![](https://github.com/DarkMagicXYZ/Java-2018f-pics/blob/master/b2.png) 

内部类KeyEventHandler用以处理键盘输入

synchronized的fight和move保证同时只有一个生物可以攻击和移动，以防止资源冲突，
初始化函数主要有gui、生物摆放、线程添加和开始的几种类型，
有相应gui刷新函数和结束的函数 guiEnd() guiRefresh()，
有读取和写入函数bufferReplay()、replay()和writeRecord()，
以及一些战场的标志函数例如isEnd()等

### 测试文件

#### TestTest

用于测试一下junit的使用，没有实际的意义

## 个人的一点感想

作为一个外院系选修的学生，这次的大作业算是我写过最复杂的代码工程，无论是面向对象的设计思想还是一些库的使用方法对我来说都有不小的难度，但从一项项功能方法入手中，确实收获良多，实战是有用的。希望这门课的经历对我以后的学习生活有所帮助，感谢老师的教导，助教的评阅，新年快乐！
