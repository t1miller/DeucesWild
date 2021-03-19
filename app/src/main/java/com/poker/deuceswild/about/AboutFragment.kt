package com.poker.deuceswild.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.poker.deuceswild.R
import com.poker.deuceswild.settings.SettingsFragment


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AboutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        val buttonEmail = view.findViewById<Button>(R.id.emailButton)

        buttonEmail.setOnClickListener {
            sendEmail()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun sendEmail() {
        val intent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "trentrobison@gmail.com", null
            )
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wild Idea")
        intent.putExtra(Intent.EXTRA_TEXT, "Hi Trent, I have a fun feature idea.")
        startActivity(Intent.createChooser(intent, "Choose an Email client :"))
    }


    companion object {

        val NAME = SettingsFragment::class.java.simpleName
    }
}