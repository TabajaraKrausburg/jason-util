!start.
+!start
<-
    .wait(10000);
    jason.actions.stopapp;
    .

!wait.
+!wait <-
    wait.

+task(Id, Colour) : not busy <-
    +busy;
    !prepared;
    !holding(Colour);
    !processed;
    !delivered;
    !deliveryChecked(Id).

+!prepared : task(Colour) & holding(Block) & not colour(Block, Colour) <-
    putDown;
    !prepared.

+!prepared <-
    // !charged;
    !reset.

+!charged[H|T] : energy(MyEnergy) & MyEnergy < 80 <-
    .print("charging ",MyEnergy);
    recharge;
    !charged[H|T];
    .
+!charged[wasDoing(G)] : energy(MyEnergy) <-
    .print("My energy is ", MyEnergy);
    .resume(G);
    // .resume(found(_));
    .

+!holding(Colour) : holding(Block) & colour(Block, Colour) <-
    .print("That's good.").

+!holding(Colour) : colour(Block,Colour) <-
    gotoBlock(Block);
    pickUp.

+!holding(Colour) : not colour(_, Colour) <-
    !found(Colour);
    ?colour(Block, Colour);
    gotoBlock(Block);
    pickUp.

+!found(Colour) : not colour(_, Colour) & place(Place) & not visited(Place) <-
    goto(Place);
    +visited(Place);
    !found(Colour).

+!found(Colour) : colour(_, Colour) <-
    wait.

+!processed : not packaging <-
    wait.

+!processed : packaging <-
    goto(packing);
    putDown;
    activate.

+!delivered <-
    goto(dropzone);
    putDown.

+!deliveryChecked(Id) : delivered(Id) <-
    ?delivered(Id);
    -busy.

+!deliveryChecked(_) <-
    .print("I am Error.");
    jason.actions.bug;
    .

+!reset <-
    .abolish(visited(_)).

@stop2[atomic]
^!found(Terms)[state(pending)]
    : .intend(charged)
<- 
    .print("I already intend to charge!");    
    .suspend(found(Terms));
    .
@stop[atomic]
^!found(Terms)[state(pending)]
    : energy(MyEnergy) & MyEnergy < 50
<- 
    .print("I need to charge! ");    
    !!charged[wasDoing(found(Terms))];
    .suspend(found(Terms));
    .

-!P[A]
<-  
    .print("Failed ",P," because ", A);
    jason.actions.stopapp(A);
    False==True; // do not recover from the failure!
    .