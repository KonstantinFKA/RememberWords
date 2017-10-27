package com.fka.rememberwords;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//класс определяет разделители отображаемые между элементами

public class ItemDivider extends RecyclerView.ItemDecoration {
    private final Drawable divider;

    //конструктор загружает встроенный разделительэлементов списка
    public ItemDivider(Context context) {
        int[] attrs = {android.R.attr.listDivider};
        divider = context.obtainStyledAttributes(attrs).getDrawable(0);
    }

    //рисование разделителей элементов списка в RecyclerView
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        //вычисление координат x для всех разделителей
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        //для каждого элемента кроме последнего нарисовать линию
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View item = parent.getChildAt(i); //получить i элемент списка

            //вычисление координат у текущего разделителя
            int top = item.getBottom() + ((RecyclerView.LayoutParams) item.getLayoutParams()).bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            //рисование разделителя с вычеслиными границами
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
