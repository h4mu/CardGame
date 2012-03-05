/*
 * Uno.java
 *
 * Created on November 21, 2004, 5:49 PM
 */

package Uno;

/**
 *
 * @author  hamu
 */
public class Uno extends java.applet.Applet {

////** Initializes the applet Uno */////////////////////////////////////////////
    public void init() {
        cs = new cardListener();

        java.awt.Panel table = new java.awt.Panel();
        top = new Card(Card.UNO, java.awt.Color.BLACK);
        java.awt.Panel communication = new java.awt.Panel();
        sayUNO = new java.awt.Button();
        whoSaid = new java.awt.Label();
        java.awt.Button deck = new java.awt.Button();
        playerView = new java.awt.ScrollPane();
        reqColour = new java.awt.Choice();

        setLayout(new java.awt.GridLayout(2, 0));

        table.setLayout(new java.awt.GridLayout(1, 0));

        top.setLabel("UNO");
        top.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topPutCard(evt);
            }
        });

        table.add(top);

        communication.setLayout(new java.awt.GridLayout(3, 0));

        sayUNO.setLabel("OK");
        sayUNO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UNOsaid(evt);
            }
        });

        communication.add(sayUNO);

        whoSaid.setAlignment(java.awt.Label.CENTER);
        whoSaid.setText("Number of players?");
        communication.add(whoSaid);

        reqColour.add("two");
        reqColour.add("three");
        reqColour.add("four");
        reqColour.add("five");
        reqColour.add("six");
        reqColour.add("seven");
        reqColour.add("eight");
        reqColour.add("nine");
        reqColour.add("ten");
        
        communication.add(reqColour);
        
        table.add(communication);

        deck.setFont(new java.awt.Font("Dialog", 1, 24));
        deck.setLabel("UNO");
        deck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deckGetCard();
            }
        });

        table.add(deck);

        add(table);

        add(playerView);        
    }
////////////////////////////////////////////////////////////////////////////////
    private void shuffle() {
        Card tmpCard = new Card((int)java.lang.Math.round(java.lang.Math.random()*14.0-5.0),
                        Card.colours[(int)java.lang.Math.round((java.lang.Math.random()*3.0))]);
        tmpCard.addMouseListener(cs);
        System.out.println(Integer.toString(player)+tmpCard);
        hands[player].add(tmpCard);
        validate();
    }
////////////////////////////////////////////////////////////////////////////////
    private class cardListener extends java.awt.event.MouseAdapter {
        public void mouseClicked(java.awt.event.MouseEvent evt) {   // select clicked card
            if(!paused) {
                if(selectedCard != null)
                    selectedCard.setSelected(false);
                selectedCard = (Card) evt.getSource();
                java.lang.System.out.println(selectedCard.toString());
                selectedCard.setSelected(true);
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void deckGetCard() {
        if(!paused) {
            shuffle();
            nextRound();
        }
    }
////////////////////////////////////////////////////////////////////////////////       
    public void topPutCard(java.awt.event.MouseEvent evt) {
        java.lang.System.out.println(top);
        if(selectedCard != null) {
            selectedCard.setSelected(false);
            if(selectedCard.canBePutOn(top)) {
                hands[player].remove(selectedCard);
                if(hands[player].getComponentCount()<=0) {
                    if(UNOsSaid[player]) {
                        UNOsSaid[player]=false;
                        out[player]=true;
                        hands[player].add(new java.awt.Label("Player "+Integer.toString(player+1)+" WON!"));
                    } else
                        for(int i=0; i<=6; i++)
                            shuffle();
                }
                validate();
                top.setEqual(selectedCard);
                selectedCard = null;
                if(top.getNumber() == Card.JOKER || top.getNumber() == Card.PLUS4) {
                    whoSaid.setText("Request a colour");
                    sayUNO.setLabel("OK"); 
                    reqColour.setEnabled(true);
                    paused = true;
                } else
                    nextRound();
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////
    private void nextRound() {
        playerView.remove(hands[player]);
        
        switch(top.getNumber())
        {
            case Card.PLUS2:
            case Card.PLUS4:
                numToPickUp -= top.getNumber();
                break;
            case Card.REVERSE:
                playerInc *= -1;
                break;
            case Card.SKIP:
                playerInc *= 2;
            default:
        }
        player += playerInc;
        if(player >= numPlayers)
            player -= numPlayers;
        else if(player < 0)
            player += numPlayers;
        if(playerInc == 2 || playerInc == -2)
            playerInc /= 2;
        if(numToPickUp>0) {
            for(int i=0; i<=numToPickUp; i++)
                shuffle();
            numToPickUp=0;
        }
        
//        roundsSinceUNOwasSaid--; a per-player one needed
            
        whoSaid.setText("Player "+Integer.toString(player+1)+"'s turn");
        sayUNO.setLabel("OK");
        paused=true;            
    }
////////////////////////////////////////////////////////////////////////////////
    public void UNOsaid(java.awt.event.MouseEvent evt) {
        if(paused) {
            System.out.println("NoItems: "+Integer.toString(reqColour.getItemCount()));
            if(reqColour.getItemCount()>4) {
                if(reqColour.getSelectedIndex()<0)
                    return;
                numPlayers=reqColour.getSelectedIndex()+2;
                System.out.println("numPlayers: "+Integer.toString(numPlayers));
                hands = new java.awt.Panel[numPlayers];
                UNOsSaid = new boolean[numPlayers];
                out = new boolean[numPlayers];
                for(player=0; player<=numPlayers-1; player++) {
                    System.out.println("player: "+Integer.toString(player+1));
                    hands[player] = new java.awt.Panel();
                    UNOsSaid[player]=false;
                    out[player] = false;
                    hands[player].setLayout(new java.awt.GridLayout(1, 0));
                    for(int i=0; i<=6; i++)
                        shuffle();
                }
                reqColour.removeAll();
                reqColour.add("Blue");
                reqColour.add("Green");
                reqColour.add("Red");
                reqColour.add("Orange");
                player=0;
                playerView.add(hands[player]);
                validate();
            } else if(whoSaid.getText().indexOf("Player") >= 0 && sayUNO.getLabel().indexOf("OK") >= 0) {
                int i=0, j=0;
                for(; i<=numPlayers-1; i++)
                    if(!out[i])
                        j++;
                if(j<=1)
                    whoSaid.setText("Player "+Integer.toString(player+1)+" LOST!");
                else
                    if(out[player]) {
                        nextRound();
                        return;
                    } else {
                        playerView.add(hands[player]);
                        validate();
                    }
            } else if(reqColour.getItemCount() == 4 && whoSaid.getText().indexOf("colour") >= 0)
                if(reqColour.getSelectedIndex()<0)
                    return;
                else {
                    top.setColour(reqColour.getSelectedIndex());
                    nextRound();
                    return;
                }
                
            whoSaid.setText("Player "+Integer.toString(player+1)+"'s turn");
            sayUNO.setLabel("Say UNO!");
        } else if(hands[player].getComponentCount() == 2) {
            UNOsSaid[player]=true;
            whoSaid.setText("Player said UNO!");
        }
        reqColour.setEnabled(false);
        paused=false;
    }
////////////////////////////////////////////////////////////////////////////////   
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        setLayout(new java.awt.GridLayout(2, 0));

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private boolean paused = true;
//    private long UNOsSaid =0;
    private int numToPickUp = 0;
    private boolean UNOsSaid[];
    private boolean out[];
    private int playerInc = 1;
    private int numPlayers = 0;
    private int player = 0;
    private cardListener cs;
    private Card selectedCard;
    private java.awt.Panel hands[];
    private java.awt.ScrollPane playerView;
    private java.awt.Button sayUNO;
    private Card top;
    private java.awt.Label whoSaid;
    private java.awt.Choice reqColour;
}
