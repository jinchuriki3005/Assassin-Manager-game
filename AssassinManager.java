//Max Tran CSE 143

import java.io.*;
import java.util.*;

/*This program make two linked lists that represent kill ring and graveyard to keep track of who is stalking
whom and who kill whom during the assassin game. When a player is killed, he/she
is moved from the kill ring to the graveyard. The game ends when there is only one 
player alive in the kill ring and he/she becomes the winner
*/
public class AssassinManager {
   private AssassinNode killRingFirst; //field
   private AssassinNode graveyardFirst; //field

   /*Use the list of names in the ArrayList names to make a kill ring
   throw IllegalArgumentException if the list of names is empty*/
   public AssassinManager(ArrayList<String> names){
      AssassinNode culprit = null;
      AssassinNode current = null;
      killRingFirst = new AssassinNode(names.get(0));
      culprit = killRingFirst;       
      if (names.isEmpty()) {
         throw new IllegalArgumentException("No players have been found.");
      }
      //set up the kill ring and define who's the killer of who. 
      for(int i = 1; i <names.size(); i++){
         current = new AssassinNode(names.get(i));
         culprit.next = current;
         current.killer = culprit.name;
         culprit = current;
      }
      //define who's the killer(the last person) of the 1st person on the list      
      killRingFirst.killer = names.get(names.size()-1);      
   }
   
   /*print out the names of the (alive) people in the kill ring and their targets.
   When there are only 2 players alive, one player will stalk the 1st person of the kill ring
   If there's only one survivor, he/she will stalk him/herself*/
   public void printKillRing(){
      AssassinNode alivePlayer = killRingFirst;
      if(killRingFirst.next != null){
         while(alivePlayer != null){
            if(alivePlayer.next == null){
               System.out.println("  " + alivePlayer.name + " is stalking " + killRingFirst.name);
            } else {
               System.out.println("  " + alivePlayer.name + " is stalking " + alivePlayer.next.name);
            }               
            alivePlayer = alivePlayer.next;
         }
      } else {
         System.out.println("  " + killRingFirst.name + " is stalking " + killRingFirst.name);
      }
   }
   
   /*print out the names of the people in the graveyard and their killer
   in the order that the most recent killed is on the top.
   Print nothing if the graveyard is empty*/
   public void printGraveyard(){      
      AssassinNode deadPlayer = graveyardFirst;
      AssassinNode temp = graveyardFirst;//a temporary file to store the name of the "real" killer
      while(deadPlayer != null){         
         while(temp.next != null && graveyardContains(deadPlayer.killer)){
            deadPlayer.killer = temp.next.killer;
            temp = temp.next;            
         }
         System.out.println("  " + deadPlayer.name + " was killed by " + deadPlayer.killer);         
         deadPlayer = deadPlayer.next;
      }      
   }
   
   //to determine whether this person is still in the kill ring aka alive
   public boolean killRingContains(String name){
      AssassinNode aliveName = killRingFirst;
      while(aliveName != null){
         if(aliveName.name.toLowerCase().equals(name.toLowerCase())){
            return true;
         }
         aliveName = aliveName.next;
      }
      return false;
   }
   
   //to determine whether this person is in the graveyard
   public boolean graveyardContains(String name){
      AssassinNode deadName = graveyardFirst;
      while(deadName != null){
         if(deadName.name.toLowerCase().equals(name.toLowerCase())){
            return true;
         }
         deadName = deadName.next;
      }
      return false;
   }
   
   //determine whether game is over or not. 
   //Return true if there is only 1 person is in the kill ring.
   public boolean isGameOver(){
      return killRingFirst.next == null;
   }
   
   //return the name of the winner of the game or null if the game isn't over
   public String winner(){
      if(!isGameOver()){
         return null;
      } else {
         return killRingFirst.name;
      }
   }
   
   /*To move a player from the kill ring to the graveyard.
   Throw IllegalArgumentException if the player isn't in the kill ring
   or IllegalStateException if the game is over*/
   public void kill(String name){
      AssassinNode current1 = null;
      AssassinNode current2 = killRingFirst;    
      if(isGameOver()){
         throw new IllegalStateException("Woohoo!! Game Over!");
      } else if(!killRingContains(name)){
         throw new IllegalArgumentException("This player is not found!");
      }    
      while (current2 != null) {
         if (current2.name.toLowerCase().equals(name.toLowerCase())) {
            if (current2.equals(killRingFirst)) {
               killRingFirst = killRingFirst.next;
            } else if (current2.next == null) {
               current1.next = null;
            } else {
               current1.next = current2.next;
            }                
            if (graveyardFirst == null) {
               graveyardFirst = current2;
               graveyardFirst.next = null;
            } else {
               current2.next = graveyardFirst;
               graveyardFirst = current2;
            }              
         } else {
            if (current1 == null) {
               current1 = killRingFirst;
            } else {
               current1 = current1.next;
            }
         }
         current2 = current2.next;
      }
   }
}