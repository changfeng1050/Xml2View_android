package com.dvc.xml;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dvc.xml.XmlDynamicProperty.TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dvc on 17/7/2017.
 * Helper function that apply properties in views
 */
@SuppressLint("NewApi")
public class XmlDynamicUtils {

    /**
     * 动态设置View中与布局属性不相关的样式属性
     * apply dynamic properties that are not relative with layout in view
     *
     * @param view
     * @param properties
     */
    public static String applyStyleProperties(View view, List<XmlDynamicProperty> properties) {
        String id = "";
        for (XmlDynamicProperty dynProp : properties) {
            switch (dynProp.field) {
                case ID: {
                    id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                }
                break;
                case BACKGROUND: {
                    applyBackground(view, dynProp);
                }
                break;
                case TEXT: {
                    applyText(view, dynProp);
                }
                break;
                case HINT:{
                    applyHint(view, dynProp);
                }
                case TEXTCOLOR: {
                    applyTextColor(view, dynProp);
                }
                break;
                case TEXTCOLORHINT: {
                    applyTextColorHint(view, dynProp);
                }
                break;
                case TEXTSIZE: {
                    applyTextSize(view, dynProp);
                }
                break;
                case TEXTSTYLE: {
                    applyTextStyle(view, dynProp);
                }
                break;
                case PADDING: {
                    applyPadding(view, dynProp);
                }
                break;
                case PADDINGLEFT: {
                    applyPadding(view, dynProp, 0);
                }
                break;
                case PADDINGTOP: {
                    applyPadding(view, dynProp, 1);
                }
                break;
                case PADDINGRIGHT: {
                    applyPadding(view, dynProp, 2);
                }
                break;
                case PADDINGBOTTOM: {
                    applyPadding(view, dynProp, 3);
                }
                break;
                case MINWIDTH: {
                    applyMinWidth(view, dynProp);
                }
                break;
                case MINHEIGHT: {
                    applyMinHeight(view, dynProp);
                }
                break;
                case MAXWIDTH: {
                    applyMaxWidth(view, dynProp);
                }
                break;
                case MAXHEIGHT: {
                    applyMaxHeight(view, dynProp);
                }
                break;
                case ELLIPSIZE: {
                    applyEllipsize(view, dynProp);
                }
                break;
                case MAXLINES: {
                    applyMaxLines(view, dynProp);
                }
                break;
                case MAXLENGHT: {
                    applyMaxLenght(view, dynProp);
                }
                break;
                case ORIENTATION: {
                    applyOrientation(view, dynProp);
                }
                break;
                case WEIGHTSUM: {
                    applyWeightSum(view, dynProp);
                }
                break;
                case GRAVITY: {
                    applyGravity(view, dynProp);
                }
                break;
                case SRC: {
                    applySrc(view, dynProp);
                }
                break;
                case SCALETYPE: {
                    applyScaleType(view, dynProp);
                }
                break;
                case INPUTTYPE: {
                    applyInputType(view, dynProp);
                }
                break;
                case ADJUSTVIEWBOUNDS: {
                    applyAdjustBounds(view, dynProp);
                }
                break;
                case DRAWABLELEFT: {
                    applyCompoundDrawable(view, dynProp, 0);
                }
                break;
                case DRAWABLETOP: {
                    applyCompoundDrawable(view, dynProp, 1);
                }
                break;
                case DRAWABLERIGHT: {
                    applyCompoundDrawable(view, dynProp, 2);
                }
                break;
                case DRAWABLEBOTTOM: {
                    applyCompoundDrawable(view, dynProp, 3);
                }
                break;
                case MAX: {
                    appSeekbarMax(view, dynProp);
                }
                break;
                case PROGRESS: {
                    appSeekbarProgress(view, dynProp);
                }
                break;
                case SPLITTRACK: {
                    appSplittrack(view, dynProp);
                }
                break;
                case THUMB: {
                    appSeekbarThumb(view, dynProp);
                }
                break;
                case THUMBOFFSET: {
                    appSeekbarThumbOffset(view, dynProp);
                }
                break;
                case PROGRESSDRAWABLE: {
                    applyProgressDrawable(view, dynProp);
                }
                break;
                case ENABLED: {
                    applyEnabled(view, dynProp);
                }
                break;
                case SELECTED: {
                    applySelected(view, dynProp);
                }
                break;
                case CLICKABLE: {
                    applyClickable(view, dynProp);
                }
                break;
                case SCALEX: {
                    applyScaleX(view, dynProp);
                }
                break;
                case SCALEY: {
                    applyScaleY(view, dynProp);
                }
                break;
                case TAG: {
                    applyTag(view, dynProp);
                }
                break;
                case VISIBILITY:{
                    applyVisibility(view, dynProp);
                }
                break;
                case FUNCTION:{
                    applyFunction(view, dynProp);
                }
                break;
            }
        }
        return id;
    }

    /**
     * 动态设置View的布局属性
     * apply dynamic properties for layout in view
     *
     * @param view
     * @param properties : layout properties to apply
     * @param viewGroup  : parent view
     * @param ids        : hashmap of ids <String, Integer> (string as setted in json, int that we use in layout)
     */
    public static void applyLayoutProperties(View view, List<XmlDynamicProperty> properties, ViewGroup viewGroup, HashMap<String, Integer> ids) {
//        if (viewGroup == null)
//            return;
        ViewGroup.LayoutParams params = createLayoutParams(viewGroup);

        for (XmlDynamicProperty dynProp : properties) {
            try {
                switch (dynProp.field) {
                    case LAYOUT_HEIGHT: {
                        switch (dynProp.type) {
                            case PATH: {
                                params.height = (int) AssetsResUtils.getAssetValue(view.getContext(), dynProp.getValueString());
                                break;
                            }
                            case DIMEN:
                            case INTEGER: {
                                params.height = dynProp.getValueInt();
                                break;
                            }
                        }
                    }
                    break;
                    case LAYOUT_WIDTH: {
                        switch (dynProp.type) {
                            case PATH: {
                                params.width = (int) AssetsResUtils.getAssetValue(view.getContext(), dynProp.getValueString());
                                break;
                            }
                            case DIMEN:
                            case INTEGER: {
                                params.width = dynProp.getValueInt();
                                break;
                            }
                        }
                    }
                    break;
                    case LAYOUT_MARGIN: {
                        if (params instanceof ViewGroup.MarginLayoutParams) {
                            ViewGroup.MarginLayoutParams p = ((ViewGroup.MarginLayoutParams) params);
                            p.bottomMargin = p.topMargin = p.leftMargin = p.rightMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.LinearLayout.LayoutParams) {
                            android.widget.LinearLayout.LayoutParams p = ((android.widget.LinearLayout.LayoutParams) params);
                            p.bottomMargin = p.topMargin = p.leftMargin = p.rightMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.FrameLayout.LayoutParams) {
                            android.widget.FrameLayout.LayoutParams p = ((android.widget.FrameLayout.LayoutParams) params);
                            p.bottomMargin = p.topMargin = p.leftMargin = p.rightMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.RelativeLayout.LayoutParams) {
                            android.widget.RelativeLayout.LayoutParams p = ((android.widget.RelativeLayout.LayoutParams) params);
                            p.bottomMargin = p.topMargin = p.leftMargin = p.rightMargin = dynProp.getValueInt();
                        }
                    }
                    break;
                    case LAYOUT_MARGINLEFT: {
                        if (params instanceof ViewGroup.MarginLayoutParams) {
                            ((ViewGroup.MarginLayoutParams) params).leftMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.LinearLayout.LayoutParams) {
                            ((LinearLayout.LayoutParams) params).leftMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.FrameLayout.LayoutParams) {
                            ((android.widget.FrameLayout.LayoutParams) params).leftMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.RelativeLayout.LayoutParams) {
                            ((android.widget.RelativeLayout.LayoutParams) params).leftMargin = dynProp.getValueInt();
                        }
                    }
                    break;
                    case LAYOUT_MARGINTOP: {
                        if (params instanceof ViewGroup.MarginLayoutParams) {
                            ((ViewGroup.MarginLayoutParams) params).topMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.LinearLayout.LayoutParams) {
                            ((android.widget.LinearLayout.LayoutParams) params).topMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.FrameLayout.LayoutParams) {
                            ((android.widget.FrameLayout.LayoutParams) params).topMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.RelativeLayout.LayoutParams) {
                            ((android.widget.RelativeLayout.LayoutParams) params).topMargin = dynProp.getValueInt();
                        }
                    }
                    break;
                    case LAYOUT_MARGINRIGHT: {
                        if (params instanceof ViewGroup.MarginLayoutParams) {
                            ((ViewGroup.MarginLayoutParams) params).rightMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.LinearLayout.LayoutParams) {
                            ((android.widget.LinearLayout.LayoutParams) params).rightMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.FrameLayout.LayoutParams) {
                            ((android.widget.FrameLayout.LayoutParams) params).rightMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.RelativeLayout.LayoutParams) {
                            ((android.widget.RelativeLayout.LayoutParams) params).rightMargin = dynProp.getValueInt();
                        }
                    }
                    break;
                    case LAYOUT_MARGINBOTTOM: {
                        if (params instanceof ViewGroup.MarginLayoutParams) {
                            ((ViewGroup.MarginLayoutParams) params).bottomMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.LinearLayout.LayoutParams) {
                            ((android.widget.LinearLayout.LayoutParams) params).bottomMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.FrameLayout.LayoutParams) {
                            ((android.widget.FrameLayout.LayoutParams) params).bottomMargin = dynProp.getValueInt();
                        }else if(params instanceof android.widget.RelativeLayout.LayoutParams) {
                            ((android.widget.RelativeLayout.LayoutParams) params).bottomMargin = dynProp.getValueInt();
                        }
                    }
                    break;
                    case LAYOUT_ABOVE: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ABOVE, ids.get(id));
                    }
                    break;
                    case LAYOUT_BELOW: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.BELOW, ids.get(id));
                    }
                    break;
                    case LAYOUT_TOLEFTOF: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.LEFT_OF, ids.get(id));
                    }
                    break;
                    case LAYOUT_TORIGHTOF: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.RIGHT_OF, ids.get(id));
                    }
                    break;
                    case LAYOUT_TOSTARTOF: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.START_OF, ids.get(id));
                    }
                    break;
                    case LAYOUT_TOENDOF: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.END_OF, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNBASELINE: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_BASELINE, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNLEFT: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_LEFT, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNTOP: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_TOP, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNRIGHT: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_RIGHT, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNBOTTOM: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_BOTTOM, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNSTART: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_START, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNEND: {
                        String id = dynProp.getValueString().indexOf("/")>-1?dynProp.getValueString().split("/")[1]:dynProp.getValueString();
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_END, ids.get(id));
                    }
                    break;
                    case LAYOUT_ALIGNWITHPARENTIFMISSING: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).alignWithParent = dynProp.getValueBoolean();
                    }
                    break;
                    case LAYOUT_ALIGNPARENTTOP: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    }
                    break;
                    case LAYOUT_ALIGNPARENTBOTTOM: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    }
                    break;
                    case LAYOUT_ALIGNPARENTLEFT: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    }
                    break;
                    case LAYOUT_ALIGNPARENTRIGHT: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }
                    break;
                    case LAYOUT_ALIGNPARENTSTART: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_START);
                    }
                    break;
                    case LAYOUT_ALIGNPARENTEND: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_END);
                    }
                    break;
                    case LAYOUT_CENTERHORIZONTAL: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.CENTER_HORIZONTAL);
                    }
                    break;
                    case LAYOUT_CENTERVERTICAL: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.CENTER_VERTICAL);
                    }
                    break;
                    case LAYOUT_CENTERINPARENT: {
                        if (params instanceof RelativeLayout.LayoutParams)
                            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                    break;
                    case LAYOUT_GRAVITY: {
                        switch (dynProp.type) {
                            case DIMEN:
                            case INTEGER: {
                                if (params instanceof LinearLayout.LayoutParams)
                                    ((LinearLayout.LayoutParams) params).gravity = dynProp.getValueInt();
                                else if (params instanceof FrameLayout.LayoutParams)
                                    ((FrameLayout.LayoutParams) params).gravity = dynProp.getValueInt();
                            }
                            break;
                            case STRING: {
                                if(dynProp.getValueString().contains("|")){
                                    String[] values = dynProp.getValueString().split("\\|");
                                    if (params instanceof LinearLayout.LayoutParams)
                                        ((LinearLayout.LayoutParams) params).gravity = Gravity.NO_GRAVITY;
                                    else if (params instanceof FrameLayout.LayoutParams)
                                        ((FrameLayout.LayoutParams) params).gravity = Gravity.NO_GRAVITY;
                                    for(String value: values){
                                        if (params instanceof LinearLayout.LayoutParams)
                                            ((LinearLayout.LayoutParams) params).gravity |= (Integer) dynProp.getValueInt(Gravity.class, value.toUpperCase());
                                        else if (params instanceof FrameLayout.LayoutParams)
                                            ((FrameLayout.LayoutParams) params).gravity |= (Integer) dynProp.getValueInt(Gravity.class, value.toUpperCase());
                                    }
                                }else {
                                    if (params instanceof LinearLayout.LayoutParams)
                                        ((LinearLayout.LayoutParams) params).gravity = (Integer) dynProp.getValueInt(Gravity.class, dynProp.getValueString().toUpperCase());
                                    else if (params instanceof FrameLayout.LayoutParams)
                                        ((FrameLayout.LayoutParams) params).gravity = (Integer) dynProp.getValueInt(Gravity.class, dynProp.getValueString().toUpperCase());
                                }
                            }
                            break;
                        }
                    }
                    break;
                    case LAYOUT_WEIGHT: {
                        switch (dynProp.type) {
                            case FLOAT: {
                                if (params instanceof LinearLayout.LayoutParams)
                                    ((LinearLayout.LayoutParams) params).weight = dynProp.getValueFloat();
                            }
                            break;
                        }
                    }
                    break;
                }
            } catch (Exception e) {
            }
        }

        view.setLayoutParams(params);
    }

    public static ViewGroup.LayoutParams createLayoutParams(ViewGroup viewGroup) {
        ViewGroup.LayoutParams params = null;
        if (viewGroup!=null) {
            try {
                /* find parent viewGroup and create LayoutParams of that class */
                Class layoutClass = viewGroup.getClass();
                while (!classExists(layoutClass.getName() + "$LayoutParams")) {
                    layoutClass = layoutClass.getSuperclass();
                }
                String layoutParamsClassname = layoutClass.getName() + "$LayoutParams";
                Class layoutParamsClass = Class.forName(layoutParamsClassname);
                /* create the actual layoutParams object */
                params = (ViewGroup.LayoutParams) layoutParamsClass.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(new Object[]{ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (params == null)
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return params;
    }

    /*** View Properties ***/

    /**
     * apply background in view. possible type :
     * - COLOR
     * - REF => search for that drawable in resources
     * - BASE64 => convert base64 to bitmap and apply in view
     */
    public static void applyBackground(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case COLOR: {
                    view.setBackgroundColor(property.getValueColor());
                }
                break;
                case REF: {
                    view.setBackgroundResource(getDrawableId(view.getContext(), property.getValueString()));
                }
                break;
                case BASE64: {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                        view.setBackground(property.getValueBitmapDrawable());
                    else
                        view.setBackgroundDrawable(property.getValueBitmapDrawable());
                }
                break;
                case DRAWABLE: {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                        view.setBackground(property.getValueGradientDrawable());
                    else
                        view.setBackgroundDrawable(property.getValueGradientDrawable());
                }
                break;
                case PATH: {
                    if (property.getValueString().toLowerCase().contains("@color")) {
                        view.setBackgroundColor((Integer) AssetsResUtils.getAssetValue(view.getContext(),property.getValueString()));
                    } else {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                            view.setBackground(com.dvc.xml.AssetsResUtils.getAssetDrawable(view.getContext(), property.getValueString()));
                        else
                            view.setBackgroundDrawable(com.dvc.xml.AssetsResUtils.getAssetDrawable(view.getContext(), property.getValueString()));
                    }
                }
                break;
            }
        }
    }

    /**
     * apply padding in view
     */
    public static void applyPadding(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case DIMEN: {
                    int padding = property.getValueInt();
                    view.setPadding(padding, padding, padding, padding);
                }
                break;
                case PATH: {
                    int padding = (int) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString());
                    view.setPadding(padding, padding, padding, padding);
                }
                break;
            }
        }
    }

    /**
     * apply padding in view
     */
    public static void applyPadding(View view, XmlDynamicProperty property, int position) {
        if (view != null) {
            switch (property.type) {
                case DIMEN: {
                    int[] padding = new int[] {
                            view.getPaddingLeft(),
                            view.getPaddingTop(),
                            view.getPaddingRight(),
                            view.getPaddingBottom()
                    };
                    padding[position] = property.getValueInt();
                    view.setPadding(padding[0], padding[1], padding[2], padding[3]);
                }
                break;
                case PATH: {
                    int[] padding = new int[] {
                            view.getPaddingLeft(),
                            view.getPaddingTop(),
                            view.getPaddingRight(),
                            view.getPaddingBottom()
                    };
                    padding[position] = (int) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString());
                    view.setPadding(padding[0], padding[1], padding[2], padding[3]);
                }
                break;
            }
        }
    }

    /**
     * apply minimum Width in view
     */
    public static void applyMinWidth(View view, XmlDynamicProperty property) {
        if (view != null) {
            if (property.type == TYPE.DIMEN) {
                view.setMinimumWidth(property.getValueInt());
            }
            else if (property.type == TYPE.PATH) {
                view.setMinimumWidth((int) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
            }
        }
    }

    /**
     * apply minimum Height in view
     */
    public static void applyMinHeight(View view, XmlDynamicProperty property) {
        if (view != null) {
            if (property.type == TYPE.DIMEN) {
                view.setMinimumHeight(property.getValueInt());
            }
            else if (property.type == TYPE.PATH) {
                view.setMinimumHeight((int) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
            }
        }
    }

    /**
     * apply maxmum Width in view(not support)
     */
    public static void applyMaxWidth(View view, XmlDynamicProperty property) {
        if (view != null) {
            if (property.type == TYPE.DIMEN) {
                Class clazz = view.getClass().getSuperclass().getSuperclass();
                try {
                    Field f = clazz.getDeclaredField("mMaxWidth");
                    f.setAccessible(true);
                    f.set(view,property.getValueInt());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            else if (property.type == TYPE.PATH) {
                Class clazz = view.getClass().getSuperclass().getSuperclass();
                try {
                    Field f = clazz.getDeclaredField("mMaxWidth");
                    f.setAccessible(true);
                    f.set(view,AssetsResUtils.getAssetValue(view.getContext(),property.getValueString()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * apply maxmum Height in view
     */
    public static void applyMaxHeight(View view, XmlDynamicProperty property) {
        if (view != null) {
            if (property.type == TYPE.DIMEN) {
                Class clazz = view.getClass().getSuperclass().getSuperclass();
                try {
                    Field f = clazz.getDeclaredField("mMaxHeight");
                    f.setAccessible(true);
                    f.set(view,property.getValueInt());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            else if (property.type == TYPE.PATH) {
                Class clazz = view.getClass().getSuperclass().getSuperclass();
                try {
                    Field f = clazz.getDeclaredField("mMaxHeight");
                    f.setAccessible(true);
                    f.set(view,AssetsResUtils.getAssetValue(view.getContext(),property.getValueString()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * apply enabled in view
     */
    public static void applyEnabled(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case BOOLEAN: {
                    view.setEnabled(property.getValueBoolean());
                }
                break;
            }
        }
    }

    /**
     * apply selected in view
     */
    public static void applySelected(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case BOOLEAN: {
                    view.setSelected(property.getValueBoolean());
                }
                break;
            }
        }
    }
    /**
     * apply clickable in view
     */
    public static void applyClickable(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case BOOLEAN: {
                    view.setClickable(property.getValueBoolean());
                }
                break;
            }
        }
    }

    /**
     * apply scalex in view
     */
    public static void applyScaleX(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case DIMEN: {
                    view.setScaleX(property.getValueFloat());
                }
                break;
            }
        }
    }

    /**
     * apply scaley in view
     */
    public static void applyScaleY(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case DIMEN: {
                    view.setScaleY(property.getValueFloat());
                }
                break;
            }
        }
    }

    /**
     *  apply visibility in view
     */
    private static void applyVisibility(View view, XmlDynamicProperty property) {
        if (view != null) {
            switch (property.type) {
                case STRING: {
                    switch (property.getValueString()){
                        case "gone":{
                            view.setVisibility(View.GONE);
                        }
                        break;
                        case "visible":{
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                        case "invisible":{
                            view.setVisibility(View.INVISIBLE);
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    /*** TextView Properties ***/

    /**
     * apply text (used only in TextView)
     * - STRING : the actual string to set in textView
     * - REF : the FIELD of string resource to apply in textView
     */
    public static void applyText(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            switch (property.type) {
                case STRING: {
                    ((TextView) view).setText(property.getValueString());
                }
                break;
                case REF: {
                    ((TextView) view).setText(getStringId(view.getContext(), property.getValueString()));
                }
                break;
                case PATH: {
                    ((TextView) view).setText((String) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply hint (used only in TextView)
     * - STRING : the actual string to set in textView hint
     * - REF : the FIELD of string resource to apply in textView hint
     */
    public static void applyHint(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            switch (property.type) {
                case STRING: {
                    ((TextView) view).setHint(property.getValueString());
                }
                break;
                case REF: {
                    ((TextView) view).setHint(getStringId(view.getContext(), property.getValueString()));
                }
                break;
                case PATH: {
                    ((TextView) view).setHint((String) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply the color in textView
     */
    public static void applyTextColor(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            switch (property.type) {
                case COLOR: {
                    ((TextView) view).setTextColor(property.getValueColor());
                }
                break;
                case PATH: {
                    ((TextView) view).setTextColor((int) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply the Hintcolor in TextView
     */
    public static void applyTextColorHint(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            switch (property.type) {
                case COLOR: {
                    ((TextView) view).setHintTextColor(property.getValueColor());
                }
                break;
                case PATH: {
                    ((TextView) view).setHintTextColor((int) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply the textSize in textView
     */
    public static void applyTextSize(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            switch (property.type) {
                case DIMEN: {
                    ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, property.getValueFloat());
                }
                break;
                case PATH: {
                    ((TextView) view).setTextSize((int) com.dvc.xml.AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }
    /**
     * apply the textStyle in textView
     */
    public static void applyTextStyle(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            switch (property.type) {
                case INTEGER: {
                    ((TextView) view).setTypeface(null, property.getValueInt());
                }
                break;
            }
        }
    }

    /**
     * apply ellipsize property in textView
     */
    public static void applyEllipsize(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            ((TextView) view).setEllipsize(TextUtils.TruncateAt.valueOf(property.getValueString().toUpperCase().trim()));
        }
    }

    /**
     * apply maxLines property in textView
     */
    public static void applyMaxLines(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            ((TextView) view).setMaxLines(property.getValueInt());
        }
    }

    /**
     * apply maxLenght property in textView
     */
    public static void applyMaxLenght(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            ((TextView) view).setFilters(new InputFilter[]{new InputFilter.LengthFilter(property.getValueInt())});
        }
    }

    /**
     * apply inputType property in textView
     */
    public static void applyInputType(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            int inputtype = InputType.TYPE_NULL;
            switch(property.getValueString()){
                case "none":
                    inputtype = InputType.TYPE_NULL;
                    break;
                case "text":
                    inputtype = InputType.TYPE_CLASS_TEXT;
                    break;
                case "textCapCharacters":
                    inputtype = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
                    break;
                case "textCapWords":
                    inputtype = InputType.TYPE_TEXT_FLAG_CAP_WORDS;
                    break;
                case "textCapSentences":
                    inputtype = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
                    break;
                case "textAutoCorrect":
                    inputtype = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
                    break;
                case "textAutoComplete":
                    inputtype = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
                    break;
                case "textMultiLine":
                    inputtype = InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                    break;
                case "textImeMultiLine":
                    inputtype = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
                    break;
                case "textNoSuggestions":
                    inputtype = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
                    break;
                case "textUri":
                    inputtype = InputType.TYPE_TEXT_VARIATION_URI;
                    break;
                case "textEmailAddress":
                    inputtype = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                    break;
                case "textEmailSubject":
                    inputtype = InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT;
                    break;
                case "textShortMessage":
                    inputtype = InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE;
                    break;
                case "textLongMessage":
                    inputtype = InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE;
                    break;
                case "textPersonName":
                    inputtype = InputType.TYPE_TEXT_VARIATION_PERSON_NAME;
                    break;
                case "textPostalAddress":
                    inputtype = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS;
                    break;
                case "textPassword":
                    inputtype = InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    break;
                case "textVisiblePassword":
                    inputtype = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                    break;
                case "textWebEditText":
                    inputtype = InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT;
                    break;
                case "textFilter":
                    inputtype = InputType.TYPE_TEXT_VARIATION_FILTER;
                    break;
                case "textPhonetic":
                    inputtype = InputType.TYPE_TEXT_VARIATION_PHONETIC;
                    break;
                case "textWebEmailAddress":
                    inputtype = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS;
                    break;
                case "textWebPassword":
                    inputtype = InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                    break;
                case "number":
                    inputtype = InputType.TYPE_CLASS_NUMBER;
                    break;
                case "numberSigned":
                    inputtype = InputType.TYPE_NUMBER_FLAG_SIGNED;
                    break;
                case "numberDecimal":
                    inputtype = InputType.TYPE_NUMBER_FLAG_DECIMAL;
                    break;
                case "numberPassword":
                    inputtype = InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                    break;
                case "phone":
                    inputtype = InputType.TYPE_CLASS_PHONE;
                    break;
                case "datetime":
                    inputtype = InputType.TYPE_CLASS_DATETIME;
                    break;
                case "date":
                    inputtype = InputType.TYPE_DATETIME_VARIATION_DATE;
                    break;
                case "time":
                    inputtype = InputType.TYPE_DATETIME_VARIATION_TIME;
                    break;
            }
            ((TextView) view).setInputType(inputtype);
        }
    }

    /**
     * apply gravity property in textView
     * - INTEGER => valus of gravity in @link(Gravity.java)
     * - STRING => FIELD of variable in @lin(Gravity.java)
     */
    public static void applyGravity(View view, XmlDynamicProperty property) {
        if (view instanceof TextView) {
            switch (property.type) {
                case INTEGER: {
                    ((TextView) view).setGravity(property.getValueInt());
                }
                break;
                case STRING: {
                    if(property.getValueString().contains("|")){
                        String[] values = property.getValueString().split("\\|");
                        int gravity = Gravity.NO_GRAVITY;
                        for(String value: values){
                            gravity |= (Integer) property.getValueInt(Gravity.class, value.toUpperCase());
                        }
                        ((TextView) view).setGravity(gravity);
                    }else {
                        ((TextView) view).setGravity((Integer) property.getValueInt(Gravity.class, property.getValueString().toUpperCase()));
                    }
                }
                break;
            }
        }
    }

    /**
     * apply compound property in textView
     * position 0:left, 1:top, 2:right, 3:bottom
     * - REF : drawable to load as compoundDrawable
     * - BASE64 : decode as base64 and set as CompoundDrawable
     */
    public static void applyCompoundDrawable(View view, XmlDynamicProperty property, int position) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            Drawable[] d = textView.getCompoundDrawables();
            switch (property.type) {
                case REF: {
                    try {
                        d[position] = view.getContext().getResources().getDrawable(getDrawableId(view.getContext(), property.getValueString()));
                    } catch (Exception e) {}
                }
                break;
                case BASE64: {
                    d[position] = property.getValueBitmapDrawable();
                }
                break;
                case DRAWABLE: {
                    d[position] = property.getValueGradientDrawable();
                }
                break;
                case PATH: {
                    d[position] = com.dvc.xml.AssetsResUtils.getAssetDrawable(view.getContext(), property.getValueString());
                }
                break;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(d[0], d[1], d[2], d[3]);
        }
    }


    /*** ImageView Properties ***/

    /**
     * apply src property in imageView
     * - REF => FIELD of drawable
     * - BASE64 => decode value as base64 image
     */
    public static void applySrc(View view, XmlDynamicProperty property) {
        if (view instanceof ImageView) {
            switch (property.type) {
                case REF: {
                    ((ImageView) view).setImageResource(getDrawableId(view.getContext(), property.getValueString()));
                }
                break;
                case BASE64: {
                    ((ImageView) view).setImageBitmap(property.getValueBitmap());
                }
                break;
                case PATH: {
                    ((ImageView) view).setImageDrawable(com.dvc.xml.AssetsResUtils.getAssetDrawable(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply scaleType property in ImageView
     */
    public static void applyScaleType(View view, XmlDynamicProperty property) {
        if (view instanceof ImageView) {
            switch (property.type) {
                case STRING: {
                    String value = property.getValueString().toUpperCase().trim();
                    try{
                        ((ImageView) view).setScaleType(ImageView.ScaleType.valueOf(value));
                    }catch(IllegalArgumentException e) {
                        if(value.contains("CENTERCROP")){
                            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        } else if(value.contains("CENTERINSIDE")){
                            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        } else if(value.contains("FITCENTER")){
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        } else if(value.contains("FITEND")){
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_END);
                        } else if(value.contains("FITSTART")){
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_START);
                        } else if(value.contains("FITXY")){
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                        }else{
                            throw new InflateException("dvc: \""+e+"\"");
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * apply adjustBounds property in ImageView
     */
    public static void applyAdjustBounds(View view, XmlDynamicProperty property) {
        if (view instanceof ImageView) {
            switch (property.type) {
                case BOOLEAN: {
                    ((ImageView) view).setAdjustViewBounds(property.getValueBoolean());
                }
                break;
            }
        }
    }

    /*** SeekBar Properties ***/

    /**
     * apply Max property in SeekBar
     */
    private static void appSeekbarMax(View view, XmlDynamicProperty property) {
        if (view instanceof SeekBar) {
            switch (property.type) {
                case INTEGER: {
                    ((SeekBar) view).setMax(property.getValueInt());
                }
                break;
                case PATH: {
                    ((SeekBar) view).setMax((Integer) AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply Progress property in SeekBar
     */
    private static void appSeekbarProgress(View view, XmlDynamicProperty property) {
        if (view instanceof SeekBar) {
            switch (property.type) {
                case INTEGER: {
                    ((SeekBar) view).setProgress(property.getValueInt());
                }
                break;
                case PATH: {
                    ((SeekBar) view).setProgress((Integer) AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply Splittrack property in SeekBar
     */
    private static void appSplittrack(View view, XmlDynamicProperty property) {
        if (view instanceof SeekBar) {
            switch (property.type) {
                case BOOLEAN: {
                    ((SeekBar) view).setSplitTrack(property.getValueBoolean());
                }
                break;

            }
        }
    }

    /**
     * apply Thumb property in SeekBar
     */
    private static void appSeekbarThumb(View view, XmlDynamicProperty property) {
        if (view instanceof SeekBar) {
            switch (property.type) {
                case REF: {
                }
                break;
                case BASE64: {
                    ((SeekBar) view).setThumb(property.getValueBitmapDrawable());
                }
                break;
                case DRAWABLE: {
                    ((SeekBar) view).setThumb(property.getValueGradientDrawable());
                }
                break;
                case PATH: {
                    ((SeekBar) view).setThumb(com.dvc.xml.AssetsResUtils.getAssetDrawable(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply ThumbOffset property in SeekBar
     */
    private static void appSeekbarThumbOffset(View view, XmlDynamicProperty property) {
        if (view instanceof SeekBar) {
            switch (property.type) {
                case DIMEN: {
                    ((SeekBar) view).setThumbOffset(property.getValueInt());
                }
                break;
                case PATH: {
                    ((SeekBar) view).setThumbOffset((Integer) AssetsResUtils.getAssetValue(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /**
     * apply ProgressDrawable property in SeekBar
     */
    public static void applyProgressDrawable(View view, XmlDynamicProperty property) {
        if (view instanceof SeekBar) {
            switch (property.type) {
                case REF: {
                }
                break;
                case BASE64: {
                    ((SeekBar) view).setProgressDrawable(property.getValueBitmapDrawable());
                }
                break;
                case DRAWABLE: {
                    ((SeekBar) view).setProgressDrawable(property.getValueGradientDrawable());
                }
                break;
                case PATH: {
                    ((SeekBar) view).setProgressDrawable(com.dvc.xml.AssetsResUtils.getAssetDrawable(view.getContext(), property.getValueString()));
                }
                break;
            }
        }
    }

    /*** CustomView Properties ***/

    /**
     * apply custom attributes property in CustomView
     */
    private static void applyFunction(View view, XmlDynamicProperty property) {
        try {
            Method method = null;
            switch (property.type) {
                case INTEGER:
                case COLOR: {
                    method = getMethod(view.getClass(), property.getFunctionStr(), int.class);
                }
                break;
                case BOOLEAN:{
                    method = getMethod(view.getClass(), property.getFunctionStr(), Boolean.class);
                }
                break;
                case DRAWABLE:{
                    method = getMethod(view.getClass(), property.getFunctionStr(), Drawable.class);
                }
                break;
                case STRING:{
                    method = getMethod(view.getClass(), property.getFunctionStr(), String.class);
                }
                break;
                case DIMEN:
                case FLOAT:{
                    method = getMethod(view.getClass(), property.getFunctionStr(), float.class);
                }
                break;
                case PATH:{
                    Object value = AssetsResUtils.getAssetValue(view.getContext(), property.getValueString());
                    if(value instanceof String){
                        method = getMethod(view.getClass(), property.getFunctionStr(), String.class);
                    }
                    else if(value instanceof Float){
                        method = getMethod(view.getClass(), property.getFunctionStr(), Float.class);
                    }
                    else if(value instanceof Integer){
                        method = getMethod(view.getClass(), property.getFunctionStr(), Integer.class);
                    }
                    method.setAccessible(true);
                    method.invoke(view, value);
                }
                return;
                case NO_VALID: {
                    method = getMethod(view.getClass(), property.getFunctionStr());
                    method.setAccessible(true);
                    method.invoke(view);
                }
                return;
            }
            method.setAccessible(true);
            method.invoke(view, property.getValueObject());
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** LinearLayout Properties ***/

    /**
     * apply orientation property in LinearLayout
     * - INTEGER => 0:Horizontal , 1:Vertical
     * - STRING
     */
    public static void applyOrientation(View view, XmlDynamicProperty property) {
        if (view instanceof LinearLayout) {
            switch (property.type) {
                case INTEGER: {
                    ((LinearLayout) view).setOrientation(property.getValueInt() == 0 ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
                }
                break;
                case STRING: {
                    ((LinearLayout) view).setOrientation(property.getValueString().equalsIgnoreCase("HORIZONTAL") ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
                }
                break;
            }
        }
    }

    /**
     * apply WeightSum property in LinearLayout
     */
    public static void applyWeightSum(View view, XmlDynamicProperty property) {
        if ((view instanceof LinearLayout) && (property.type == TYPE.FLOAT)) {
            ((LinearLayout) view).setWeightSum(property.getValueFloat());
        }
    }

    /**
     * add string as tag
     */
    public static void applyTag(View view, XmlDynamicProperty property) {
        view.setTag(property.getValueString());
    }

    /**
     * return the id (from the R.java autogenerated class) of the drawable that pass its FIELD as argument
     */
    public static int getDrawableId(Context context, String name) {
        return getR_Id(context, "drawable", name);
    }

    /**
     * return the id (from the R.java autogenerated class) of the string that pass its FIELD as argument
     */
    public static int getStringId(Context context, String name) {
        return getR_Id(context, "string", name);
    }

    /**
     * return the id (from the R.java autogenerated class) of the layout that pass its FIELD as argument
     */
    public static int getLayoutId(Context context, String name) {
        return getR_Id(context, "layout", name);
    }

    public static int getR_Id(Context context, String defType, String name) {
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    /**
     * convert densityPixel to pixel
     */
    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * convert scalePixel to pixel
     */
    public static float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * convert pixel to densityPixel
     */
    public static float pxToDp(int px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * convert pixel to scaledDensityPixel
     */
    public static float pxToSp(int px) {
        return (px / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    /**
     * convert densityPixel to scaledDensityPixel
     */
    public static float dpToSp(float dp) {
        return (int) ( dpToPx(dp) / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    public static int getDeviceWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * get ViewHolder class and make reference for evert @link(DynamicViewId) to the actual view
     * if target contains HashMap<String, Integer> will replaced with the idsMap
     */
    public static void parseDynamicView(Object target, View container, HashMap<String, Integer> idsMap) {

        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(XmlDynamicViewId.class)) {
                field.setAccessible(true);
                /* if variable is annotated with @XmlDynamicViewId */
                final XmlDynamicViewId dynamicViewIdAnnotation = field.getAnnotation(XmlDynamicViewId.class);
                /* get the Id of the view. if it is not set in annotation user the variable name */
                String id = dynamicViewIdAnnotation.id();
                if (id.equalsIgnoreCase(""))
                    id = field.getName();
                if (idsMap.containsKey(id)) {
                    try {
                        /* get the view Id from the Hashmap and make the connection to the real View */
                        field.set(target, container.findViewById(idsMap.get(id)));
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } else if ((field.getName().equalsIgnoreCase("ids")) && (field.getType() == idsMap.getClass())) {
                try {
                    field.set(target, idsMap);
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static Object getFromJSON(JSONObject json, String name, Class clazz) throws JSONException {
        if ((clazz == Integer.class)||(clazz == Integer.TYPE)) {
            return json.getInt(name);
        } else if ((clazz == Boolean.class)||(clazz == Boolean.TYPE)) {
            return json.getBoolean(name);
        } else if ((clazz == Double.class)||(clazz == Double.TYPE)) {
            return json.getDouble(name);
        } else if ((clazz == Float.class)||(clazz == Float.TYPE)) {
            return (float)json.getDouble(name);
        } else if ((clazz == Long.class)||(clazz == Long.TYPE)) {
            return json.getLong(name);
        } else if (clazz == String.class) {
            return json.getString(name);
        } else if (clazz == JSONObject.class) {
            return json.getJSONObject(name);
        } else {
            return json.get(name);

        }
    }

    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch(ClassNotFoundException ex) {
            return false;
        }
    }

    public static Method getMethod(Class clazz, String methodName,
                                   final Class... classes) throws Exception {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, classes);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName,
                            classes);
                }
            }
        }
        return method;
    }

}
