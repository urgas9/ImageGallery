package com.etiennelawlor.imagegallery.library.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.data.ImageData;
import com.etiennelawlor.imagegallery.library.enums.PaletteColorType;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.etiennelawlor.imagegallery.library.view.PaletteTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by etiennelawlor on 8/20/15.
 */
public class FullScreenImageGalleryAdapter extends PagerAdapter {

    // region Member Variables
    private final List<ImageData> mImages;
    private final PaletteColorType mPaletteColorType;
    private final boolean mShowComments;
    // endregion

    // region Constructors
    public FullScreenImageGalleryAdapter(List<ImageData> images, PaletteColorType paletteColorType, boolean showComments) {
        mImages = images;
        mPaletteColorType = paletteColorType;
        mShowComments = showComments;
    }
    // endregion

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.fullscreen_image, null);

        final ImageView imageView = (ImageView) view.findViewById(R.id.iv);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl);

        final ImageData imageData = mImages.get(position);

        final Context context = imageView.getContext();
        int width = ImageGalleryUtils.getScreenWidth(context);

        if (!TextUtils.isEmpty(imageData.getImageUrl())) {
            Picasso.with(imageView.getContext())
                    .load(imageData.getImageUrl())
                    .resize(width, 0)
                    .transform(PaletteTransformation.instance())
                    .into(imageView, new PaletteTransformation.PaletteCallback(imageView) {
                        @Override
                        public void onError() {
                            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_photo_size_select_actual_white_48dp));
                            imageView.setScaleType(ImageView.ScaleType.CENTER);
                        }

                        @Override
                        public void onSuccess(Palette palette) {
                            int bgColor = getBackgroundColor(palette);
                            if (bgColor != -1)
                                relativeLayout.setBackgroundColor(bgColor);
                        }
                    });
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_photo_size_select_actual_white_48dp));
        }


        if(mShowComments) {
            if (imageData.getComment() != null) {
                ((TextView) view.findViewById(R.id.tv_comment)).setText(imageData.getComment());
            }
            else{
                view.findViewById(R.id.tv_comment).setVisibility(View.GONE);
            }

            final ImageView userAvatarImageView = (ImageView) view.findViewById(R.id.iv_avatar);
            Picasso.with(context)
                    .load(imageData.getUserAvatarUrl())
                    .error(R.drawable.ic_account_circle_white_24dp)
                    .into(userAvatarImageView);

            if (imageData.getUserName() != null) {
                ((TextView) view.findViewById(R.id.tv_name)).setText(imageData.getUserName());
            }

            if (imageData.getTimePosted() != null) {
                ((TextView) view.findViewById(R.id.tv_date)).setText(imageData.getTimePosted());
            }

            final ImageButton favoriteButton = (ImageButton) view.findViewById(R.id.btn_favorite_me);
            if (imageData.isLiked()) {
                setButtonDrawable(favoriteButton, context, R.drawable.ic_favorite_white_36dp);
            }

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Send action to server
                    if (!imageData.isLiked()) {
                        setButtonDrawable(favoriteButton, context, R.drawable.ic_favorite_white_36dp);
                        imageData.setLiked(true);
                    } else {
                        setButtonDrawable(favoriteButton, context, R.drawable.ic_favorite_border_white_36dp);
                        imageData.setLiked(false);
                    }
                }
            });
        }
        else{
            view.findViewById(R.id.btn_favorite_me).setVisibility(View.GONE);
            view.findViewById(R.id.ll_comment).setVisibility(View.GONE);
        }


        container.addView(view, 0);

        return view;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // region Helper Methods
    private void setButtonDrawable(ImageButton ib, Context context, int resourceId){
        ib.setImageDrawable(ContextCompat.getDrawable(context, resourceId));
    }
    private int getBackgroundColor(Palette palette) {
        int bgColor = -1;

        int vibrantColor = palette.getVibrantColor(0x000000);
        int lightVibrantColor = palette.getLightVibrantColor(0x000000);
        int darkVibrantColor = palette.getDarkVibrantColor(0x000000);

        int mutedColor = palette.getMutedColor(0x000000);
        int lightMutedColor = palette.getLightMutedColor(0x000000);
        int darkMutedColor = palette.getDarkMutedColor(0x000000);

        if (mPaletteColorType != null) {
            switch (mPaletteColorType) {
                case VIBRANT:
                    if (vibrantColor != 0) { // primary option
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) { // fallback options
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    } else if (mutedColor != 0) {
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    }
                    break;
                case LIGHT_VIBRANT:
                    if (lightVibrantColor != 0) { // primary option
                        bgColor = lightVibrantColor;
                    } else if (vibrantColor != 0) { // fallback options
                        bgColor = vibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    } else if (mutedColor != 0) {
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    }
                    break;
                case DARK_VIBRANT:
                    if (darkVibrantColor != 0) { // primary option
                        bgColor = darkVibrantColor;
                    } else if (vibrantColor != 0) { // fallback options
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (mutedColor != 0) {
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    }
                    break;
                case MUTED:
                    if (mutedColor != 0) { // primary option
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) { // fallback options
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    } else if (vibrantColor != 0) {
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    }
                    break;
                case LIGHT_MUTED:
                    if (lightMutedColor != 0) { // primary option
                        bgColor = lightMutedColor;
                    } else if (mutedColor != 0) { // fallback options
                        bgColor = mutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    } else if (vibrantColor != 0) {
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    }
                    break;
                case DARK_MUTED:
                    if (darkMutedColor != 0) { // primary option
                        bgColor = darkMutedColor;
                    } else if (mutedColor != 0) { // fallback options
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (vibrantColor != 0) {
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    }
                    break;
                default:
                    break;
            }
        }

        return bgColor;
    }
    // endregion
}
