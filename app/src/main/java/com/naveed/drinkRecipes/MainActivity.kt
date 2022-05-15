package com.naveed.drinkRecipes

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naveed.drinkRecipes.databinding.ActivityMainBinding
import com.naveed.drinkRecipes.model.Drinks
import com.naveed.drinkRecipes.reciever.AlarmBroadcastReceiver
import com.naveed.drinkRecipes.viewModel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    var modelRandom : Drinks ?= null

    private val viewModelFavourite : FavouriteViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                binding?.titleText?.text = getString(R.string.drink_recipes)

            } else {
                binding?.titleText?.text = getString(R.string.fav_recipes)
            }
        }

        setAlarmDaily()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarmDaily()
    {
        val calNow = Calendar.getInstance()
        val calSet = calNow.clone() as Calendar
        calSet.set(Calendar.HOUR_OF_DAY, 14)
        calSet.set(Calendar.MINUTE, 0)
        calSet.set(Calendar.SECOND, 0)
        calSet.set(Calendar.MILLISECOND, 0)

        if (calSet <= calNow) {
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1)
        }

         viewModelFavourite.getRandomRecord().observe(this) {
            if(it != null)
            {
                modelRandom = it

                val intent = Intent(this, AlarmBroadcastReceiver::class.java)
                intent.putExtra("name", modelRandom?.name ?: "Need Some Drink Open App Now")
                intent.putExtra("instruction",  modelRandom?.instruction ?: "")
                intent.putExtra("path",  modelRandom?.imageOffline)

                var pendingIntent : PendingIntent ?= null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    pendingIntent = PendingIntent.getBroadcast(
                        baseContext,
                        1100,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }
                else {
                    pendingIntent = PendingIntent.getBroadcast(
                        baseContext,
                        1010,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmManager[AlarmManager.RTC_WAKEUP, calSet.timeInMillis] = pendingIntent

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calSet.timeInMillis, // you can just add System.currentTimeMillis()+15000 to check working
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
             else
            {
                val intent = Intent(this, AlarmBroadcastReceiver::class.java)
                intent.putExtra("name", "Need Some Drink Open App Now")
                intent.putExtra("instruction",   "Explore how to Make Your Best Drink")
                intent.putExtra("path","")

                var pendingIntent : PendingIntent ?= null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    pendingIntent = PendingIntent.getBroadcast(
                        baseContext,
                        1100,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }
                else {
                    pendingIntent = PendingIntent.getBroadcast(
                        baseContext,
                        1010,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmManager[AlarmManager.RTC_WAKEUP, calSet.timeInMillis] = pendingIntent

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calSet.timeInMillis, // you can just add System.currentTimeMillis()+15000 to check working
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        }


    }


}