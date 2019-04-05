package com.man.manmusic.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.man.manmusic.Model.ItemSong;
import com.man.manmusic.MyActivity;
import com.man.manmusic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SongListAdapter extends BaseAdapter {
    private ArrayList<ItemSong> arraySong;
    private LayoutInflater mInflater;
    private Context mContext;
    private Bitmap image;

    public SongListAdapter(Context context, ArrayList<ItemSong> newArraySong) {
        this.arraySong = newArraySong;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void addSong() {
        ItemSong itemSong = new ItemSong(1, "Duanaonhi", "Hahahaa", "Yeeee", "GiangHoang", "222",66);
        arraySong.add(itemSong);
    }

    @Override
    public int getCount() {
        return arraySong.size();
    }

    @Override
    public ItemSong getItem(int position) {
        return arraySong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.itemsong2, parent, false);
            holder = new ViewHolder();
            holder.tvSongName = (TextView) convertView.findViewById(R.id.tv_SongName);
            holder.tvSongArtist = (TextView) convertView.findViewById(R.id.tv_SongArtist);
            holder.tvSongTime = (TextView) convertView.findViewById(R.id.tv_SongTime);
            holder.btSetting = (ImageView) convertView.findViewById(R.id.bt_Setting);
            holder.ivHeart = (ImageView) convertView.findViewById(R.id.iv_LoveSong);
            holder.songImage= (ImageView) convertView.findViewById(R.id.songImage);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        holder.tvSongName.setText(arraySong.get(position).getTitle());
        holder.tvSongArtist.setText(arraySong.get(position).getArtist());
        holder.btSetting.setImageResource(R.drawable.dots);
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        holder.tvSongTime.setText(dateFormat.format(new Date(arraySong.get(position).getDuration())));
//        Bitmap artWork = arraySong.get(position).getSongImage();
//        if (artWork == null) {
//
//
//        } else {
//
//            // set output
//            holder.songImage.setImageBitmap(artWork);
//
//
//        }
        byte[] bytes = arraySong.get(position).getAlbum_Art(mContext);
        holder.songImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));


        final ViewHolder finalHolder = holder;
        holder.btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Remove", Toast.LENGTH_SHORT).show();
//                arraySong.remove(position);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.action);
                dialog.setTitle("Choose Action");
                Button btShowInfor = (Button) dialog.findViewById(R.id.bt_ShowInfor);
                Button btDelete = (Button) dialog.findViewById(R.id.bt_Delete);
                Button btSaveLoveSong = (Button) dialog.findViewById(R.id.bt_SaveLoveSong);
                dialog.show();
                btShowInfor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.TextAppearance_Theme_Dialog));
                        builder.setTitle("Information");
                        builder.setMessage(arraySong.get(position).getDisplayName() + "\n"
                                + arraySong.get(position).getArtist() + "\n" +
                                arraySong.get(position).getAlbum() + "\n" +
                                arraySong.get(position).getDataPath() + "\n" +
                                convertToDate(arraySong.get(position).getDuration()) + "\n")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                    }
                                });

                        // Create the AlertDialog object and return it
                        builder.show();
                        Log.i("giang", "Yaya");
                        dialog.dismiss();
                    }
                });
                btDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arraySong.remove(position);
                        MyActivity.createTable();
                        dialog.dismiss();
                    }
                });
                btSaveLoveSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalHolder.ivHeart.setImageResource(R.drawable.heart);
                        dialog.dismiss();
                    }
                });
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvSongName;
        TextView tvSongArtist;
        TextView tvSongTime;
        ImageView btSetting;
        ImageView ivHeart;
        ImageView songImage;
    }

    private String convertToDate(Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(value));
    }
}
