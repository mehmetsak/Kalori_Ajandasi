package com.mehmetsakiratasayin.dailycalorierecord;
import android.content.Context;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    public GroceryAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }
    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView countText;
        public TextView sourText;
        public TextView plusText;
        public TextView statusText;
        public GroceryViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textview_name_item);
            countText = itemView.findViewById(R.id.textview_amount_item);
            plusText = itemView.findViewById(R.id.textview_finishWin);
            sourText = itemView.findViewById(R.id.textview_finishLost);
            statusText = itemView.findViewById(R.id.textview_finishStatus);

        }


    }
    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.grocery_item, parent, false);
        return new GroceryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME));
        String amount = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT));
        String sour = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_SOUR));
        String plus = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_PLUS));
        String status = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_STATUS));
        long id = mCursor.getLong(mCursor.getColumnIndex(GroceryContract.GroceryEntry._ID));
        holder.nameText.setText(name);
        holder.statusText.setText(status);
        holder.countText.setText(amount);
        holder.sourText.setText(sour);
        holder.plusText.setText(plus);
        holder.itemView.setTag(id);

    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}