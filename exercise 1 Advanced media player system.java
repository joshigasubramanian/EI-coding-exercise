import java.util.ArrayList;
import java.util.List;

// Observer Pattern
interface Observer {
    void update(String media);
}

class MediaStation {
    private List<Observer> observers = new ArrayList<>();
    private String currentMedia;

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void setCurrentMedia(String media) {
        this.currentMedia = media;
        notifyObservers();
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(currentMedia);
        }
    }
}

class MediaDisplay implements Observer {
    @Override
    public void update(String media) {
        System.out.println("Media Display: Now playing " + media);
    }
}

// Strategy Pattern
interface PlaybackStrategy {
    void play(String filename);
}

class NormalPlayback implements PlaybackStrategy {
    @Override
    public void play(String filename) {
        System.out.println("Playing " + filename + " normally.");
    }
}

class HighQualityPlayback implements PlaybackStrategy {
    @Override
    public void play(String filename) {
        System.out.println("Playing " + filename + " in high quality.");
    }
}

class PlaybackContext {
    private PlaybackStrategy strategy;

    public PlaybackContext(PlaybackStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PlaybackStrategy strategy) {
        this.strategy = strategy;
    }

    public void play(String filename) {
        strategy.play(filename);
    }
}

// Singleton Pattern
class MediaLibrary {
    private static MediaLibrary instance;
    private List<String> mediaFiles = new ArrayList<>();

    private MediaLibrary() {}

    public static MediaLibrary getInstance() {
        if (instance == null) {
            instance = new MediaLibrary();
        }
        return instance;
    }

    public void addMedia(String media) {
        mediaFiles.add(media);
    }

    public List<String> getMediaFiles() {
        return mediaFiles;
    }
}

// Factory Method Pattern
abstract class Media {
    public abstract void play();
}

class AudioMedia extends Media {
    @Override
    public void play() {
        System.out.println("Playing audio file.");
    }
}

class VideoMedia extends Media {
    @Override
    public void play() {
        System.out.println("Playing video file.");
    }
}

abstract class MediaCreator {
    public abstract Media createMedia();
}

class AudioMediaCreator extends MediaCreator {
    @Override
    public Media createMedia() {
        return new AudioMedia();
    }
}

class VideoMediaCreator extends MediaCreator {
    @Override
    public Media createMedia() {
        return new VideoMedia();
    }
}

// Adapter Pattern
interface MediaPlayer {
    void play(String audioType, String filename);
}

interface AdvancedMediaPlayer {
    void playVlc(String filename);
    void playMp4(String filename);
}

class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String filename) {
        System.out.println("Playing vlc file. Name: " + filename);
    }

    @Override
    public void playMp4(String filename) {}
}

class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String filename) {}

    @Override
    public void playMp4(String filename) {
        System.out.println("Playing mp4 file. Name: " + filename);
    }
}

class MediaAdapter implements MediaPlayer {
    AdvancedMediaPlayer advancedMusicPlayer;

    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audioType, String filename) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(filename);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(filename);
        }
    }
}

class AudioPlayer implements MediaPlayer {
    MediaAdapter mediaAdapter;

    @Override
    public void play(String audioType, String filename) {
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file. Name: " + filename);
        } else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, filename);
        } else {
            System.out.println("Invalid media. " + audioType + " format not supported");
        }
    }
}

// Decorator Pattern
class Text {
    private String content;

    public Text(String content) {
        this.content = content;
    }

    public String render() {
        return content;
    }
}

abstract class TextDecorator extends Text {
    protected Text wrapped;

    public TextDecorator(Text wrapped) {
        super(wrapped.render());
        this.wrapped = wrapped;
    }

    public abstract String render();
}

class BoldDecorator extends TextDecorator {
    public BoldDecorator(Text wrapped) {
        super(wrapped);
    }

    @Override
    public String render() {
        return "<b>" + wrapped.render() + "</b>";
    }
}

class ItalicDecorator extends TextDecorator {
    public ItalicDecorator(Text wrapped) {
        super(wrapped);
    }

    @Override
    public String render() {
        return "<i>" + wrapped.render() + "</i>";
    }
}

class UnderlineDecorator extends TextDecorator {
    public UnderlineDecorator(Text wrapped) {
        super(wrapped);
    }

    @Override
    public String render() {
        return "<u>" + wrapped.render() + "</u>";
    }
}

// Combined Use Case
public class AdvancedMediaPlayerSystem {
    public static void main(String[] args) {
        // Observer Pattern
        MediaStation mediaStation = new MediaStation();
        MediaDisplay mediaDisplay = new MediaDisplay();
        mediaStation.addObserver(mediaDisplay);

        // Singleton Pattern
        MediaLibrary mediaLibrary = MediaLibrary.getInstance();
        mediaLibrary.addMedia("song.mp3");
        mediaLibrary.addMedia("video.mp4");

        // Factory Method Pattern
        MediaCreator audioCreator = new AudioMediaCreator();
        MediaCreator videoCreator = new VideoMediaCreator();

        Media audioMedia = audioCreator.createMedia();
        Media videoMedia = videoCreator.createMedia();

        audioMedia.play();
        videoMedia.play();

        // Strategy Pattern
        PlaybackContext playbackContext = new PlaybackContext(new NormalPlayback());
        playbackContext.play("song.mp3");

        playbackContext.setStrategy(new HighQualityPlayback());
        playbackContext.play("video.mp4");

        // Adapter Pattern
        AudioPlayer audioPlayer = new AudioPlayer();
        audioPlayer.play("mp3", "song.mp3");
        audioPlayer.play("mp4", "video.mp4");
        audioPlayer.play("vlc", "movie.vlc");
        audioPlayer.play("avi", "example.avi");

        // Decorator Pattern
        Text simpleText = new Text("Now playing: song.mp3");
        Text boldText = new BoldDecorator(simpleText);
        Text italicText = new ItalicDecorator(boldText);
        Text underlineText = new UnderlineDecorator(italicText);

        System.out.println(simpleText.render());
        System.out.println(boldText.render());
        System.out.println(italicText.render());
        System.out.println(underlineText.render());

        // Observer Pattern - Notify about current media
        mediaStation.setCurrentMedia("song.mp3");
        mediaStation.setCurrentMedia("video.mp4");
    