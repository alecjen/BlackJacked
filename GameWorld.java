package com.test;

/**
 * Created by ajen1 on 11/10/16.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameWorld {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JLabel chipCountLabel;
    private JPanel controlPanel;

    private JButton hitButton;
    private JButton stayButton;
    private JButton dealButton;

    private Shoe shoe;
    private Better better;
    private Dealer dealer;

    public GameWorld(){
        prepareGUI();
        this.hitButton = new JButton( "Hit" );
        this.stayButton = new JButton( "Stay" );
        this.dealButton = new JButton( "Deal" );

        this.dealer = new Dealer( "Dealer Joe" );
        this.better = new Better( "Alec", 100, 10 );
        this.shoe = new Shoe(5);

    }

    public static void main( String[] args ){
        GameWorld gameWorld = new GameWorld();
        gameWorld.showEventDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame( "BlackJack" );
        mainFrame.setSize( 400,400 );
        mainFrame.setLayout( new GridLayout( 3, 1 ) );

        headerLabel = new JLabel( "",JLabel.CENTER );
        statusLabel = new JLabel( "",JLabel.CENTER );
        chipCountLabel = new JLabel( "Chip count: ", JLabel.NORTH_WEST );

        statusLabel.setSize( 350,100 );
        mainFrame.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowEvent ){
                System.exit(0);
            }
        } );
        controlPanel = new JPanel();
        controlPanel.setLayout( new FlowLayout() );

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    private void showEventDemo(){
        headerLabel.setText( "Good luck!" );

        this.hitButton.setActionCommand( "Hit" );
        this.stayButton.setActionCommand( "Stay" );
        this.dealButton.setActionCommand( "Deal" );

        this.hitButton.setVisible(false);
        this.stayButton.setVisible(false);

        this.hitButton.addActionListener( new ButtonClickListener() );
        this.stayButton.addActionListener( new ButtonClickListener() );
        this.dealButton.addActionListener( new ButtonClickListener() );

        controlPanel.add(hitButton);
        controlPanel.add(stayButton);
        controlPanel.add(dealButton);

        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();
            if( command.equals( "Hit" ) )  {
                this.hit();
            }
            else if( command.equals( "Stay" ) )  {
                this.stay();
            }
            else if( command.equals( "Deal" ) ) {
                this.dealHands();
            }

            String out = "";
            for( int i = 0; i < better.getCurrentHand().getCards().size(); i++ ) {
                Card c = better.getCurrentHand().getCards().get(i);
                out += c.getCardFace() + " ";
            }
            headerLabel.setText(out);
            chipCountLabel.setText( "Chip count: " + better.getChipCount() );
        }

        public void hit() {
            String text = "You hit a ";
            for( int i = 0; i < better.getHands().size(); i++ ) {
                //text += this.getHandTotal() + " ";
                Card hitCard = shoe.deal( 1, better.getCurrentHand() ).get(0);
                text += hitCard.getCardFace();
            }
            text += " .";
            if( better.getCurrentHand().didBust() ) {
                text += " You busted!";
                hitButton.setVisible(false);
                stayButton.setVisible(false);
                dealButton.setVisible(true);
            } else {
                text += " You have " + this.getHandTotal();
            }
            statusLabel.setText(text);
        }

        public void stay() {
            hitButton.setVisible(false);
            stayButton.setVisible(false);
            dealButton.setVisible(true);
            statusLabel.setText( "Stay. \n" + this.getHandTotal() );
        }

        public void dealHands() {
            better.endHand();
            shoe.deal( 2, better.getCurrentHand() );
            hitButton.setVisible(true);
            stayButton.setVisible(true);
            dealButton.setVisible(false);
            statusLabel.setText( this.getHandTotal() );
        }

        public String getHandTotal() {
            return Integer.toString( better.getCurrentHand().getHandTotal() );
        }
    }
}