package com.cuihai.demo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Conversation(
                SampleData.conversationSample
            )
        }

    }


    data class Message(val title: String, val subTitle: String)

    @Composable
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
    fun SimpleComposable(message: Message) {

        Row(modifier = Modifier
            .padding(all = 12.dp)
            .clickable {

            }) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "", modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column() {
                Text(text = message.title, style = MaterialTheme.typography.body1)
                Text(text = message.subTitle, style = MaterialTheme.typography.subtitle1)
            }
        }
        Spacer(modifier = Modifier.width(1.dp))
    }

    @Composable
    fun Conversation(messages: List<Message>) {
        Column {
            messages.forEach {
                SimpleComposable(message = it)
            }
        }
    }
}