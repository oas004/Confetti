import SwiftUI
import SwiftUIFlowLayout
import ConfettiKit


struct SessionDetailsView2: UIViewControllerRepresentable {
    var session: SessionDetails
    
    func makeUIViewController(context: Context) -> UIViewController {
        return SharedViewControllersKt.SessionDetailsViewController(session: session)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}


struct SessionDetailsView: View {
    var session: SessionDetails

    var body: some View {
        
        ScrollView {
            VStack(alignment: .leading, spacing: 6) {
                Text(session.title).font(.system(size: 24)).foregroundColor(.blue)
                //Spacer().frame(height: 8)
                
                Text(session.sessionDescription ?? "").font(.system(size: 16))
                                
                if session.tags.count > 0 {
                    FlowLayout(mode: .scrollable,
                               items: session.tags,
                               itemSpacing: 4) {
                        Text($0)
                            .padding(.vertical, 10)
                            .padding(.horizontal)
                            .background(.blue)
                            .foregroundColor(.white)
                            .background(Capsule().stroke())
                            .clipShape(Capsule())
                            .font(.system(size: 16))
                    }
                }
                
                Spacer()
                ForEach(session.speakers, id: \.self) { speaker in
                    SessionSpeakerInfo(speaker: speaker.speakerDetails)
                }
                Spacer()
            }
            .padding()
        }
        .navigationBarTitleDisplayMode(.inline)
    }
}
