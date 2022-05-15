package com.naveed.drinkRecipes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naveed.drinkRecipes.R
import com.naveed.drinkRecipes.interfaceRcv.OnFavouriteClickListener
import com.naveed.drinkRecipes.model.DrinkList
import com.naveed.drinkRecipes.model.Drinks
import com.naveed.drinkRecipes.viewModel.FavouriteViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class FavouriteDrinksAdapter(
    var context: Context,
    var drinkList: ArrayList<Drinks>,
    var onFavClick: OnFavouriteClickListener,
    var boolean: Boolean
) :
    RecyclerView.Adapter<FavouriteDrinksAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBoxAlcholic: CheckBox = itemView.findViewById(R.id.checkboxAlcholcic)
        val btnFavourite: ImageView = itemView.findViewById(R.id.btnFavourite)
        val imageDrink: CircleImageView = itemView.findViewById(R.id.profile_image)
        val drinkName: TextView = itemView.findViewById(R.id.drinkNameTxt)
        val instructionTxt: TextView = itemView.findViewById(R.id.instructionTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.rcv_item_fav, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



        holder.drinkName.text = drinkList[position].name
        holder.instructionTxt.text = drinkList[position].instruction
        holder.checkBoxAlcholic.isChecked =
            drinkList[position].alcoholic!!.contains("Alcoholic")


        if (boolean)
        {
            Glide.with(context).load(drinkList[position].image).into(holder.imageDrink)
        }
        else
        {
            Glide.with(context).load(File(context.getExternalFilesDir(null)?.path + "/${drinkList[position].id}.jpg")).into(holder.imageDrink)
        }


        holder.btnFavourite.setOnClickListener {
          onFavClick.onRemoveFavouriteClickListener(position)
        }

    }

    override fun getItemCount(): Int {
        return drinkList.size
    }
}