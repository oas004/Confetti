package dev.johnoreilly.confetti.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application
import com.seiko.imageloader.rememberAsyncImagePainter
import dev.johnoreilly.confetti.fragment.SessionDetails
import dev.johnoreilly.confetti.fragment.SpeakerDetails
import dev.johnoreilly.confetti.fullNameAndCompany
import platform.UIKit.UIViewController

fun SessionDetailsViewController(session: SessionDetails): UIViewController =
    Application("Confetti") {
        SessionDetailView(session)
    }



@Composable
internal fun SessionDetailView(session: SessionDetails?) {
    val scrollState = rememberScrollState()

    Column {
        session?.let { session ->
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(state = scrollState)
            ) {

                Text(text = session.title,
                    style = MaterialTheme.typography.h5,
                    color = Color(0, 128, 255)
                )

                Spacer(modifier = Modifier.size(16.dp))
                Text(text = session.sessionDescription ?: "",
                    style = MaterialTheme.typography.body1)


                if (session.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.size(16.dp))
                    FlowRow(crossAxisSpacing = 8.dp) {
                        session.tags.forEach { tag ->
                            Chip(tag)
                        }
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))
                session.speakers.forEach { speaker ->
                    SessionSpeakerInfo(speaker = speaker.speakerDetails,
                        onSocialLinkClick = { socialItem, speakerDetails ->
                            // TODO how to hook this up to per-platform code?
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(socialItem.link))
//                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }

}



@Composable
internal fun SessionSpeakerInfo(
    modifier: Modifier = Modifier,
    speaker: SpeakerDetails,
    onSocialLinkClick: (SpeakerDetails.Social, SpeakerDetails) -> Unit
) {
    Column(modifier.padding(top = 16.dp)) {
        Row {

            speaker.photoUrl?.let {
                val painter = rememberAsyncImagePainter(speaker.photoUrl)
                Image(
                    painter, null,
                    modifier = Modifier.size(64.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
            }

            Column(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = speaker.fullNameAndCompany(),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                speaker.city?.let { city ->
                    Text(
                        text = city,
                        style = MaterialTheme.typography.h6
                    )
                }

                speaker.bio?.let { bio ->
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = bio,
                        style = MaterialTheme.typography.body2
                    )
                }


                Row(
                    Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    speaker.socials.forEach { socialsItem ->
//                        SocialIcon(
//                            modifier = Modifier.size(24.dp),
//                            socialItem = socialsItem,
//                            onClick = { onSocialLinkClick(socialsItem, speaker) }
//                        )
                    }
                }
            }
        }
    }
}


@Composable
internal fun Chip(name: String) {
    Surface(
        modifier = Modifier.padding(end = 10.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0, 128, 255)
        //color = MaterialTheme.colors.primary
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(10.dp)
        )
    }
}
