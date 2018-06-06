import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Kakashi extends Player {

    ImageIcon RKakaStat;
    ImageIcon RKakaWalk;
    ImageIcon RKakaBlock;
    ImageIcon RKakaJump;
    ImageIcon RKakaPunch;
    ImageIcon RKakaKick;
    ImageIcon RKakaTele;
    ImageIcon RKakaChi;
    ImageIcon RKakaSummon;

    ImageIcon LKakaStat;
    ImageIcon LKakaWalk;
    ImageIcon LKakaBlock;
    ImageIcon LKakaJump;
    ImageIcon LKakaPunch;
    ImageIcon LKakaKick;
    ImageIcon LKakaTele;
    ImageIcon LKakaChi;
    ImageIcon LKakaSummon;

    ImageIcon Genjutsu = new ImageIcon("src/Resource/Kakashi/DLC/Genjutsu.gif");
    ImageIcon temp;

    int superFacing = 0;
    int countUp = 0;

    Timer pauseTimer;

    public Kakashi(JComponent RootPane, int WPN) {

        super();

        ArrayList<Integer> allkakaData = Main.rw.readKakaData();


        whichPlayerNum = WPN;

        if (WPN == 1) {

            addKeyBinder(RootPane, KeyEvent.VK_T, "P1Summon", e -> summon());

        } else {

            addKeyBinder(RootPane, KeyEvent.VK_I, "P2Summon", e -> summon());

        }

        setKakaPics(whichPlayerNum);
        setInitLoc(whichPlayerNum);
        setWhichPlayer(whichPlayerNum, RootPane);
        setMoveSpeed(allkakaData.get(0));
        setProjectSpeed(allkakaData.get(3));


        stopAct(10, e -> {


            //punch
            if (allBoolMove[1][3] && count == allkakaData.get(1)) {

                if (facingLeft()) {

                    moveHorizontal(KakaPunchDistance);

                }
                stopMoving();
                reset(1, 3);
                stopTimer.stop();

                //kick
            } else if (allBoolMove[1][4] && count == allkakaData.get(1)) {

                stopMoving();
                reset(1, 4);
                stopTimer.stop();

                //shoot
            } else if (allBoolMove[1][5] && count == allkakaData.get(2)) {

                teleport();
                stopMoving();
                reset(1, 5);
                stopTimer.stop();

                //super
            } else if (allBoolMove[0][3] && count == allkakaData.get(4)) {

                teleport(superFacing);
                chidori = true;

                //super continued
            } else if (allBoolMove[0][4] && count == 80) {

                reset(0, 4);
                stopTimer.stop();


                //super continued
            } else if (allBoolMove[0][3] && count == allkakaData.get(5)) {

                chidori = false;
                stopMoving();
                moveVertical(-KakaSuperDistance);
                reset(0, 3);

                if (whichPlayerNum == 1) {

                    Main.fightWindow.P2.reset(1, 1);
                    Main.fightWindow.P2.beingSuped = false;
                    Main.fightWindow.P2.setLocation(Main.fightWindow.P2.getX(), getY());

                } else {

                    Main.fightWindow.P1.reset(1, 1);
                    Main.fightWindow.P1.beingSuped = false;
                    Main.fightWindow.P1.setLocation(Main.fightWindow.P1.getX(), getY());

                }
                stopTimer.stop();

            }


            if (allBoolMove[0][3] && count >= allkakaData.get(4)) {

                if (superFacing == 0) {

                    moveHorizontal(-moveSpeed * 2);

                } else {

                    moveHorizontal(moveSpeed * 2);

                }


            }

            count++;

        });

        pauseAct(1000, e -> {

            if (countUp == 3) {

                dontMove = false;
                Main.fightWindow.background.setIcon(temp);
                countUp = 0;
                pauseTimer.stop();

            }

            countUp++;

        });


        movementTimer.start();

    }

    public Kakashi(JComponent RootPane, int WPN, ImageIcon[][] p, ArrayList<Integer> d) {

        whichPlayerNum = WPN;

        setKakaPics(whichPlayerNum, p);
        setInitLoc(whichPlayerNum);
        setWhichPlayer(whichPlayerNum, RootPane);
        setMoveSpeed(d.get(0));
        setProjectSpeed(d.get(3));

        stopAct(10, e -> {


            //punch
            if (allBoolMove[1][3] && count == d.get(1)) {

                if (facingLeft()) {

                    setLocation(getLocation().x + WizPunchDistance, getY());

                }
                stopMoving();
                reset(1, 3);
                stopTimer.stop();

                //kick
            } else if (allBoolMove[1][4] && count == d.get(1)) {

                stopMoving();
                reset(1, 4);
                stopTimer.stop();

                //shoot
            } else if (allBoolMove[1][5] && count == d.get(2)) {

                teleport();
                stopMoving();
                reset(1, 5);
                stopTimer.stop();

                //super
            } else if (allBoolMove[0][3] && count == d.get(4)) {

                superFacing = facing;
                teleport();
                chidori = true;

                //super continued
            } else if (allBoolMove[0][3] && count == d.get(5)) {

                chidori = false;
                stopMoving();
                moveVertical(-KakaSuperDistance);
                reset(0, 3);

                if (whichPlayerNum == 1) {

                    Main.fightWindow.P2.reset(1, 1);
                    Main.fightWindow.P2.beingSuped = false;
                    Main.fightWindow.P2.setLocation(Main.fightWindow.P2.getX(), getY());

                } else {

                    Main.fightWindow.P1.reset(1, 1);
                    Main.fightWindow.P1.beingSuped = false;
                    Main.fightWindow.P1.setLocation(Main.fightWindow.P1.getX(), getY());

                }
                stopTimer.stop();

            }

            if (allBoolMove[0][3] && count >= d.get(4)) {

                if (superFacing == 0) {

                    moveHorizontal(-moveSpeed * 2);

                } else {

                    moveHorizontal(moveSpeed * 2);

                }


            }

            count++;

        });

        movementTimer.start();

    }

    void pauseAct(int delay, ActionListener actionListener) {

        pauseTimer = new Timer(delay, e -> {

            actionListener.actionPerformed(e);

        });
    }

    void summon() {

        if (!isAttacking() && hpMagic.hasMagic(SUPER_MGI) && !atTop && !GameOver) {

            if (whichPlayerNum == 1) {

                Main.fightWindow.P2.stopMoving();

            } else {

                Main.fightWindow.P1.stopMoving();

            }

            dontMove = true;
            temp = (ImageIcon) Main.fightWindow.background.getIcon();
            Main.fightWindow.background.setIcon(imgRescaler(Genjutsu, AllWindows.width, AllWindows.height));
            hpMagic.decMagic(SUPER_MGI);
            pauseTimer.start();
            stopMoving();
            set(0, 4);
            stopTimer.start();

        }

    }

    void punch() {

        if (!isAttacking() && !GameOver) {

            punchSetback();
            super.punch();

        }

    }

    void kick() {

        if (!isAttacking() && !atTop && !GameOver) {

            super.kick();

        }

    }

    void shoot() {

        if (!isAttacking() && !isBlocking() && !GameOver) {

            super.shoot();

        }

    }

    void superPower() {

        if (!isAttacking() && hpMagic.hasMagic(SUPER_MGI) && !isBlocking() && !GameOver) {

            super.superPower();
            superSetback();
            superFacing = facing;

        }

    }

    void bulletCreation() {

    }

    void punchSetback() {

        if (facingLeft()) {

            moveHorizontal(-KakaPunchDistance);

        }

    }

    void superSetback() {

        moveVertical(KakaSuperDistance);

    }

    public void setInitLoc(int whichPlayerNum) {

        if (whichPlayerNum == 1) {

            setIcon(RKakaStat);
            setBounds(0, FightClub.height - RKakaStat.getIconHeight() - COMMON_FLOOR, RKakaStat.getIconWidth(), RKakaStat.getIconHeight());


        } else if (whichPlayerNum == 2) {

            setIcon(LKakaStat);
            setBounds(FightClub.width - LKakaStat.getIconWidth() - 30, FightClub.height - LKakaStat.getIconHeight() - COMMON_FLOOR, LKakaStat.getIconWidth(), LKakaStat.getIconHeight());

        }

    }
    //setup pics

    void setKakaPics(int whichPlayerNum) {

        RKakaStat = new ImageIcon("src/Resource/Kakashi/RKakaStat.gif");
        RKakaWalk = new ImageIcon("src/Resource/Kakashi/DLC/RKakaWalk.gif");
        RKakaBlock = new ImageIcon("src/Resource/Kakashi/DLC/RKakaBlock.gif");
        RKakaJump = new ImageIcon("src/Resource/Kakashi/DLC/RKakaJump.gif");
        RKakaPunch = new ImageIcon("src/Resource/Kakashi/DLC/RKakaPunch.gif");
        RKakaKick = new ImageIcon("src/Resource/Kakashi/DLC/RKakaKick.gif");
        RKakaTele = new ImageIcon("src/Resource/Kakashi/DLC/RKakaTeleport.gif");
        RKakaChi = new ImageIcon("src/Resource/Kakashi/DLC/RKakaSuper.gif");
        RKakaSummon = new ImageIcon("src/Resource/Kakashi/DLC/RKakaSummon.gif");

        LKakaStat = new ImageIcon("src/Resource/Kakashi/LKakaStat.gif");
        LKakaWalk = new ImageIcon("src/Resource/Kakashi/DLC/LKakaWalk.gif");
        LKakaBlock = new ImageIcon("src/Resource/Kakashi/DLC/LKakaBlock.gif");
        LKakaJump = new ImageIcon("src/Resource/Kakashi/DLC/LKakaJump.gif");
        LKakaPunch = new ImageIcon("src/Resource/Kakashi/DLC/LKakaPunch.gif");
        LKakaKick = new ImageIcon("src/Resource/Kakashi/DLC/LKakaKick.gif");
        LKakaTele = new ImageIcon("src/Resource/Kakashi/DLC/RKakaTeleport.gif");
        LKakaChi = new ImageIcon("src/Resource/Kakashi/DLC/LKakaSuper.gif");
        LKakaSummon = new ImageIcon("src/Resource/Kakashi/DLC/LKakaSummon.gif");

        //setting pics
        allPic[0][0] = RKakaStat;
        allPic[0][1] = RKakaJump;
        allPic[0][3] = RKakaChi;
        allPic[0][4] = RKakaSummon;
        allPic[1][0] = LKakaWalk;
        allPic[1][1] = RKakaBlock;
        allPic[1][2] = RKakaWalk;
        allPic[1][3] = RKakaPunch;
        allPic[1][4] = RKakaKick;
        allPic[1][5] = RKakaTele;


        allPic[2][0] = LKakaStat;
        allPic[2][1] = LKakaJump;
        allPic[2][3] = LKakaChi;
        allPic[2][4] = LKakaSummon;
        allPic[3][0] = LKakaWalk;
        allPic[3][1] = LKakaBlock;
        allPic[3][2] = RKakaWalk;
        allPic[3][3] = LKakaPunch;
        allPic[3][4] = LKakaKick;
        allPic[3][5] = LKakaTele;


        setIcon(RKakaStat);
        setBounds(0, FightClub.height - RKakaStat.getIconHeight() - COMMON_FLOOR, RKakaStat.getIconWidth(), RKakaStat.getIconHeight());

        whichCharacter[2] = true;
        hpMagic = new Bar(whichPlayerNum, whichCharacter);

    }
    //setup pics

    void setKakaPics(int whichPlayerNum, ImageIcon[][] p) {

        allPic = p;

        RKakaStat = allPic[0][0];
        LKakaStat = allPic[2][0];
        RKakaChi = allPic[0][3];

        setIcon(RKakaStat);
        setBounds(0, FightClub.height - RKakaStat.getIconHeight() - COMMON_FLOOR, RKakaStat.getIconWidth(), RKakaStat.getIconHeight());

        whichCharacter[2] = true;
        hpMagic = new Bar(whichPlayerNum, whichCharacter);

    }

    void teleport() {

        if (facingRight()) {

            setLocation(AllWindows.width - RKakaChi.getIconWidth(), getY());

        } else {

            setLocation(0, getY());

        }

    }

    void teleport(int face) {

        if (face == 0) {

            setLocation(AllWindows.width - RKakaChi.getIconWidth(), getY());

        } else {

            setLocation(0, getY());

        }

    }
    //resize images to correct size

    protected ImageIcon imgRescaler(ImageIcon img, int w, int h) {

        //complete magic here
        return new ImageIcon(img.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));

    }


}
