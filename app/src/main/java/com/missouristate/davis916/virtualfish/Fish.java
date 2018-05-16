package com.missouristate.davis916.virtualfish;

/**
 * Laura Davis CIS 262-902
 * This is the fish class. This determines the actions
 * and movements of the fish.
 */

public class Fish {
    //Fish artwork object has a current position so it can be accessed publicly
    public int x;
    public int y;

    public static final int IsHungry = 1;
    public static final int IsSwimming = 2;
    public static final int IsEating = 3;

    private int mCondition;
    private int mVelocity;
    private int mStomachCapacity;
    private int mFoodInStomach;
    private int mTankWidth;
    private int mTankHeight;
    private int mDirection;

    private int playX, playY;
    private int foodX, foodY;

    public Fish(int xPos, int yPos, int condition, int tankWidth, int tankHeight){
        mCondition = condition;
        mVelocity =  3;
        mStomachCapacity = 80;
        mFoodInStomach = mStomachCapacity;
        mTankWidth = tankWidth;
        mTankHeight = tankHeight;
        x = xPos;
        y = yPos;
        mDirection = 1;

        //Food and explore locations are fixed in the top and bottom of the tank
        foodY = (int) tankHeight / 2 - 100;
        foodX = (int) (Math.ceil(Math.random() * mTankWidth) -
                mTankWidth / 2);
        playY = (int) - (Math.random() * mTankHeight / 2) + 100;
        playX = (int) (Math.ceil(Math.random() * mTankWidth -
                mTankWidth / 2));
    }//end Fish class

    public void move(){
        //Examine possible conditions
        switch (mCondition){
            case IsSwimming:
                swim();
                break;
            case IsHungry:
                findFood();
                break;
            case IsEating:
                eatFood();
        }
    }//end move()

    private void swim(){
        //Burn a calorie of food
        mFoodInStomach --;

        //Swim toward a point of interest: playX and playY
        int xDistance = playX - x;
        int yDistance = playY - y;
        x += xDistance / mVelocity;
        y+= yDistance / mVelocity;
        if(playX < x){
            mDirection = -1;
        }else{
            mDirection = 1;
        }

        //Find another place to explore in the top half of the tank
        if(Math.abs(xDistance) < 5 && Math.abs(yDistance) < 5){
            playX = (int) (Math.ceil(Math.random() * mTankWidth) -
                    mTankWidth / 2);
            playY = (int) -(Math.random() * mTankHeight / 2) + 100;
        }

        //Determine if stomach is empty
        if(mFoodInStomach <= 0){
            mCondition = IsHungry;
            //Find a place to eat in the bottom of the tank
            foodX = (int) (Math.ceil(Math.random() * mTankWidth) -
                    mTankWidth / 2) - 100;
        }
    }//end swim()

    private void findFood(){
        //Swim toward food: foodX and foodY
        int xDistance = foodX - x;
        int yDistance = foodY - y;

        x += xDistance / mVelocity;
        y += yDistance / mVelocity;

        //Turn fish in direction of food
        if(foodX < x){
            mDirection = -1;
        }else{
            mDirection = 1;
        }

        //Determine if food is found
        if(Math.abs(x - foodX) <= 10 && Math.abs(y - foodY) <= 10){
            mCondition = IsEating;
        }
    }//end findFood

    private void eatFood(){
        //Add a calorie of food to the stomach
        mFoodInStomach += 4;

        //Determine if stomach is full
        if(mFoodInStomach >= mStomachCapacity){
            mCondition = IsSwimming;

            //Find a new place to play
            playX = (int) (Math.ceil(Math.random() * mTankWidth) -
                    mTankWidth / 2);
            playY = (int) -(Math.random() * mTankHeight / 2) + 100;
        }
    }//end eatFood()

    public int getFacingDirection(){
        return mDirection;
    }

}//end Fish class
