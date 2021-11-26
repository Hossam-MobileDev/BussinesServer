package com.hashtagco.bussinesserver.Service;

import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MyFirebaseInstanceIDService{ /*extends FirebaseInstanceIdService
{


    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();

        String token= FirebaseInstanceId.getInstance().getToken();

        if (Common.currentUser !=null)
            updateTokenServer(token);





    }

    private void updateTokenServer(String token)
    {

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference referenceToken=db.getReference("Tokens");


        Token token1=new Token(token,true); //parser isServerToken True because Customers

        referenceToken.child(Common.currentUser.getPhone()).setValue(token1);

    }


*/

}
