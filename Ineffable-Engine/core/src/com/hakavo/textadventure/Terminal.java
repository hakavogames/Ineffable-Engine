package com.hakavo.textadventure;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import java.io.*;

public class Terminal extends Joint {
    public Terminal() {
        super.addComponent(new Transform(20,215,0.5f,0.5f));
        super.addComponent(new TextRenderer("",Color.WHITE));
        super.addComponent(new TerminalController());
        super.addComponent(new TerminalOutput());
        super.addGameObject(new TerminalInput());
    }
    public static class TerminalOutput extends GameComponent {
        public int maxLines=21,maxWidth=68;
        protected TextRenderer textRenderer;
        private PrintStream printStream;
        private ByteArrayOutputStream baos;
        
        @Override
        public void start() {
            textRenderer=getGameObject().getComponent(TextRenderer.class);
            baos=new ByteArrayOutputStream();
            printStream=new PrintStream(baos);
            printStream.print(" ");
            System.setOut(printStream);
        }
        @Override
        public void update(float delta) {
            try {
                String text=String.valueOf(baos.toString("UTF8"));
                int newline=0,linesize=0;
                for(int k=Math.max(0,text.length()-1-maxLines*maxWidth);k<text.length();k++) {
                    if(text.charAt(k)=='\n')linesize=0;
                    else linesize++;
                    if(linesize==maxWidth) {
                        text=text.substring(0,Math.max(0,k))+"\n"+text.substring(k);
                        linesize=0;
                    }
                }
                int i=text.length()-1;
                while(newline<maxLines&&i-1>=0) {
                    if(text.charAt(i)=='\n')newline++;
                    i--;
                }
                i=Math.max(i+1,0);
                if(i<text.length()&&text.charAt(i)=='\n')i++;
                textRenderer.text=text.substring(i);
            } catch(UnsupportedEncodingException ex) {
                ex.printStackTrace();
                Gdx.app.exit();
            }
        }
        public void reset() {
            baos.reset();
        }
    }
    public static class TerminalInput extends GameObject {
        public TerminalInput() {
            addComponent(new Transform(5,10,0.5f,0.5f));
            addComponent(new TextRenderer("> ",new Color(0.9f,0.9f,0.9f,1f)));
            addComponent(new TerminalInputController());
        }
    }
    public static class TerminalInputController extends GameComponent implements InputProcessor {
        protected TextRenderer input;
        public int maxLength=64;
        public float blinkTime=0.1f;
        private InputListener listener;
        private String line="";
        private boolean reading;
        
        @Override
        public void start() {
            input=getGameObject().getComponent(TextRenderer.class);
            Gdx.input.setInputProcessor(this);
        }
        @Override
        public void update(float delta) {
            input.text="> "+line;
            if(GameServices.getElapsedTime()%(blinkTime*2)>=blinkTime&&reading)input.text+="_";
        }
        @Override
        public boolean keyTyped(char c) {
            if(reading)
            {
                if(c=='\b'&&line.length()>0)
                    line=line.substring(0,line.length()-1);
                else if(c=='\r')
                {
                    reading=false;
                    if(listener!=null)listener.onTextRead(line);
                    line="";
                }
                else if(isPrintable(c)&&line.length()+1<maxLength)
                    line+=c;
            }
            return false;
        }
        public void readLine(InputListener listener) {
            if(!reading) {
                reading=true;
                this.listener=listener;
            }
        }
        public boolean isReading() {
            return reading;
        }
        @Override
        public boolean keyDown(int i) {return false;}
        @Override
        public boolean keyUp(int i) {return false;}
        @Override
        public boolean touchDown(int i, int i1, int i2, int i3) {return false;}
        @Override
        public boolean touchUp(int i, int i1, int i2, int i3) {return false;}
        @Override
        public boolean touchDragged(int i, int i1, int i2) {return false;}
        @Override
        public boolean mouseMoved(int i, int i1) {return false;}
        @Override
        public boolean scrolled(int i) {return false;}
        private boolean isPrintable(char c) {
            Character.UnicodeBlock block=Character.UnicodeBlock.of(c);
            return (!Character.isISOControl(c)) &&
                    c!=java.awt.event.KeyEvent.CHAR_UNDEFINED&&
                    block!=null&&
                    block!=Character.UnicodeBlock.SPECIALS;
        }
        public static interface InputListener {
            public void onTextRead(String input);
        }
    }
}