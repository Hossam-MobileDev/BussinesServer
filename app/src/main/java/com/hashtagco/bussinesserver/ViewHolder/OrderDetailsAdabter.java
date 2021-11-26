package com.hashtagco.bussinesserver.ViewHolder;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.Model.Category;
import com.hashtagco.bussinesserver.Model.Order;
import com.hashtagco.bussinesserver.R;

import java.util.List;

//View Holder
class ViewHolderOrderDetails extends RecyclerView.ViewHolder
{

   public TextView Quentity,price;

   public ViewHolderOrderDetails(View itemView)
   {
       super(itemView);

       price=(TextView)itemView.findViewById(R.id.proudprice);

       Quentity=(TextView)itemView.findViewById(R.id.proudact_quentity_item);

      // Name = (TextView) itemView.findViewById(R.id.proudactname);

   }

}


//Adabter
public class OrderDetailsAdabter extends RecyclerView.Adapter<ViewHolderOrderDetails>
{

    private List<Order>orderList;
    private Context context;

    public OrderDetailsAdabter(Context context, List<Order> orderList)
    {

         this.context=context;
         this.orderList=orderList;

    }


    @NonNull
    @Override
    public ViewHolderOrderDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.order_details_item,parent,false);

        return new ViewHolderOrderDetails(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderOrderDetails holder, int position)
    {

          Order order=orderList.get(position);

        //  holder.Discount.setText(String.format("Discount : %s",order.getDiscount()));
          holder.Quentity.setText(order.getQuentity());
//holder.price.setText(order.getPrice());
     }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
