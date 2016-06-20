package com.gppmds.tra.temremdioa.controller.adapter.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gppmds.tra.temremdioa.controller.SelectMedicineActivity;
import com.gppmds.tra.temremdioa.controller.UbsMapsActivity;
import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterUBS;
import com.gppmds.tra.temremdioa.model.UBS;
import com.tra.gppmds.temremdioa.R;

public class ViewHolderUBS extends RecyclerView.ViewHolder{
    private TextView textViewUbsName;
    private TextView textViewUbsNeighborhood;
    private TextView textViewUbsCity;
    private TextView textViewUbsAttentionLevel;
    private RelativeLayout headerLayout;
    private RelativeLayout expandLayout;
    private ValueAnimator mAnimator;
    private Button buttonSelectMedicine;
    private Button buttonViewUbsDescription;
    private ImageView imageViewArrow;

    public ViewHolderUBS(CardView card) {
        super(card);
        this.textViewUbsName = (TextView) card.findViewById(R.id.textViewUBSName);
        this.textViewUbsNeighborhood = (TextView) card.findViewById(R.id.textViewUBSNeighborhood);
        this.textViewUbsCity = (TextView) card.findViewById(R.id.textViewUBSCity);
        this.textViewUbsAttentionLevel = (TextView) card.findViewById(R.id.textViewUbsAttentionLevel);
        this.imageViewArrow = (ImageView) card.findViewById(R.id.imageViewArrow);
        this.buttonSelectMedicine = (Button) card.findViewById(R.id.buttonSelectMedicine);
        this.buttonViewUbsDescription = (Button) card.findViewById(R.id.buttonUbsDescription);
        this.expandLayout = (RelativeLayout) card.findViewById(R.id.expandable);
        this.headerLayout = (RelativeLayout) card.findViewById(R.id.header);

        this.expandLayout.setVisibility(View.GONE);

        this.expandLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        expandLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        expandLayout.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
                                                View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
                                                View.MeasureSpec.UNSPECIFIED);
                        expandLayout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0, expandLayout.getMeasuredHeight());
                        return true;
                    }
                });

        this.headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LOG", "onClickListener of headerLayout clicked");
                if (expandLayout.getVisibility() == View.GONE) {
                    Log.i("LOG", "Expand Click");
                    expand();
                } else {
                    Log.i("LOG", "Collapse Click");
                    collapse();
                }
            }
        });

        this.buttonSelectMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectMedicineActivity.class);
                UBS selectItem = CardListAdapterUBS.dataUBS.get(ViewHolderUBS.this
                                    .getAdapterPosition());
                intent.putExtra("nomeUBS", selectItem.getUbsName());
                intent.putExtra("nivelAtencao", selectItem.getUbsAttentionLevel());
                v.getContext().startActivity(intent);
            }
        });

        this.buttonViewUbsDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UbsMapsActivity.class);
                UBS selectItem = CardListAdapterUBS.dataUBS.get(ViewHolderUBS.this
                                    .getAdapterPosition());
                intent.putExtra("latitude", selectItem.getUbsLatitude());
                intent.putExtra("longitude", selectItem.getUbsLongitude());
                intent.putExtra("nomeUBS", selectItem.getUbsName());
                intent.putExtra("descEnderecoUBS", selectItem.getUbsAddress());
                intent.putExtra("descBairroUBS", selectItem.getUbsNeighborhood());
                intent.putExtra("descCidadeUBS", selectItem.getUbsCity());
                v.getContext().startActivity(intent);
            }
        });
    }

    private void expand() {
        /* set Visible */
        Log.i("LOG", "Expand enter, View.VISIBLE");
        expandLayout.setVisibility(View.VISIBLE);
        mAnimator.start();
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up);
    }

    private void collapse() {
        int finalHeight = expandLayout.getHeight();

        ValueAnimator mAnimator2 = slideAnimator(finalHeight, 0);
        mAnimator2.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.i("LOG", "collapse onAnimationEnd enter, View.GONE");
                expandLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
                // Nothing to do
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Nothing to do
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Nothing to do
            }
        });
        mAnimator2.start();
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down);
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                /* Update Height */
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = expandLayout.getLayoutParams();
                layoutParams.height = value;
                expandLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public TextView getTextViewUbsName(){
        return textViewUbsName;
    }

    public TextView getTextViewUbsNeighborhood(){
        return textViewUbsNeighborhood;
    }

    public TextView getTextViewUbsCity(){
        return textViewUbsCity;
    }

    public TextView getTextViewUbsAttentionLevel(){
        return textViewUbsAttentionLevel;
    }

    public Button getButtonSelectMedicine(){
        return buttonSelectMedicine;
    }
}