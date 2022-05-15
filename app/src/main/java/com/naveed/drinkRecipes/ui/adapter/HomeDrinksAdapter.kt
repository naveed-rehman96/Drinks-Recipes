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
import com.naveed.drinkRecipes.viewModel.DrinksViewModel
import com.naveed.drinkRecipes.viewModel.FavouriteViewModel
import de.hdodenhof.circleimageview.CircleImageView

class HomeDrinksAdapter(var context: Context, var drinkList: DrinkList
, var onFavClick : OnFavouriteClickListener,var viewModel: FavouriteViewModel) :
    RecyclerView.Adapter<HomeDrinksAdapter.MyViewHolder>() {



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBoxAlcholic : CheckBox = itemView.findViewById(R.id.checkboxAlcholcic)
        val btnFavourite : ImageView = itemView.findViewById(R.id.btnFavourite)
        val imageDrink : CircleImageView = itemView.findViewById(R.id.profile_image)
        val drinkName : TextView = itemView.findViewById(R.id.drinkNameTxt)
        val instructionTxt : TextView = itemView.findViewById(R.id.instructionTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rcv_item_home, parent , false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        Glide.with(context).load(drinkList?.drinks?.get(position)?.image).into(holder.imageDrink)
        holder.drinkName.text = drinkList?.drinks?.get(position)?.name
        holder.instructionTxt.text = drinkList?.drinks?.get(position)?.instruction
        holder.checkBoxAlcholic.isChecked = drinkList?.drinks?.get(position)?.alcoholic!!.contains("Alcoholic")



        val check = viewModel.isRowExist(drinkList?.drinks?.get(position)?.id!!)

        if (check)
        {
            holder.btnFavourite.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_baseline_star_24))
        }
        else
        {
            holder.btnFavourite.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_outline_star_border_24))
        }


        holder.btnFavourite.setOnClickListener{
            if (check)
            {
                if (onFavClick.onRemoveFavouriteClickListener(position))
                {
                    holder.btnFavourite.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_outline_star_border_24))
                }

            }
           else
            {
                if (onFavClick.onFavouriteItemClickListener(position))
                {
                    holder.btnFavourite.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_baseline_star_24))
                }


            }
        }

    }

    override fun getItemCount(): Int {
       return drinkList.drinks.size
    }
}