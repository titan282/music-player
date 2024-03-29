package ie.app.musicplayer.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ie.app.musicplayer.Adapter.PlaylistDialogAdapter;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class AddToPlaylistDialog extends AppCompatDialogFragment {
    private Context context;
    private Song song;
    private List<Playlist> playlists;
    public AddToPlaylistDialog(Context context, Song song){
        this.context = context;
        this.song =song;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        deleteAllPlaylist();
        View view = inflater.inflate(R.layout.dialog_add_playlist,container,false);
        PlaylistDialogAdapter adapter = new PlaylistDialogAdapter(context);
        ListView lvPlaylistName = view.findViewById(R.id.lvPlaylistName);
        lvPlaylistName.setAdapter(adapter);
        LinearLayout newPlaylist = view.findViewById(R.id.newPlaylist);
        //Press newPlaylist
        newPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPlaylist(view);
            }
        });
        lvPlaylistName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addSongtoPlaylist(i);
                }
        });
        return view;


    }
    public void addSongtoPlaylist(int i){
        playlists = Playlist.listAll(Playlist.class);
        List<Song> songList = playlists.get(i).getSongList();
        if(checkSong(songList)){
            Toast.makeText(context,"Bài hát này đã có trong "+playlists.get(i).getPlaylistName()+" playlist!",Toast.LENGTH_SHORT).show();
            Log.v("song", "Add failed!");
        }
        else {
//            song = convertToDefaultAlbumPicture(song);
            playlists.get(i).getSongList().add(song);
            playlists.get(i).save();
            Toast.makeText(context,"Thêm bài hát vào "+playlists.get(i).getPlaylistName()+ " playlist thành công!",Toast.LENGTH_SHORT).show();
        }
        dismiss();
        Log.v("song", "Add "+playlists.get(i).getPlaylistName()+ " "+song.getSongName());
        playlists = Playlist.listAll(Playlist.class);
        Log.v("song", "songList"+ playlists.get(i).getSongList().size());
    }

    private Song covertToDefaultAlbumPicture(Song song) {
        song.setSongEmbeddedPicture(BitmapFactory.decodeResource(getResources(),R.drawable.music_rect));
        return song;
    }

    private boolean checkSong(List<Song> songList) {
        List<String> songUrl = new ArrayList<String>();
        for(Song songItem:songList){
            songUrl.add(songItem.getSongURL());
        }
        return songUrl.contains(song.getSongURL());
    }

    public void createNewPlaylist(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dismiss();
        View view1 = getActivity().getLayoutInflater().inflate(R.layout.dialog_playlist_name,null);
        EditText playlistName = view1.findViewById(R.id.etPlaylistName);
        builder.setView(view1)
                .setTitle("Tạo Playlist")
                .setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<Song> songList = new ArrayList<Song>();
                        songList.add(song);
                        Log.v("song",song.getSongName());

                        playlists = Playlist.listAll(Playlist.class);
                        boolean isHavePlaylist = false;
                        for(Playlist playlist:playlists){
                            if(playlistName.getText().toString().equals(playlist.getPlaylistName())){
                                isHavePlaylist = true;
                                break;
                            }
                            Log.d("Debug",(playlistName.getText().toString().equals(playlist.getPlaylistName()))+"");
                        }
                        if(!isHavePlaylist){
                            new Playlist(playlistName.getText().toString(),R.drawable.img_playlist_default, songList).save();
                            Toast.makeText(context, "Tạo Playlist thành công!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Thêm bài hàt vào "+playlistName.getText()+ " playlist thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,"Đã có Playlist này !",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }
    public void deleteAllPlaylist(){
        List<Playlist> books = Playlist.listAll(Playlist.class);
        Playlist.deleteAll(Playlist.class);
    }

}
