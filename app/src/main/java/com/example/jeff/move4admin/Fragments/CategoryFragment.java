package com.example.jeff.move4admin.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4admin.Library.DatabaseFunctions;
import com.example.jeff.move4admin.Library.Like;
import com.example.jeff.move4admin.Library.ServerLoader;
import com.example.jeff.move4admin.Library.ServerRequestHandler;
import com.example.jeff.move4admin.Library.adapters.AllCategoriesAdapter;
import com.example.jeff.move4admin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Like> likes = new ArrayList<Like>();
    DatabaseFunctions dbf;
    ServerLoader sl = ServerLoader.getInstance(getActivity());
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private GridView g_likes;
    private MenuItem m_add;
    private MenuItem m_save;
    private EditText e_categoryinput;
    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // loads menu
        setHasOptionsMenu(true);
        // changes keyboard to not resize ur view
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            mContext = getActivity().getApplicationContext();
            dbf = DatabaseFunctions.getInstance(getActivity());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
        m_add = menu.findItem(R.id.action_add);
        m_save = menu.findItem(R.id.action_save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                m_save.setVisible(true);
                m_add.setVisible(false);
                e_categoryinput.setVisibility(View.VISIBLE);
                e_categoryinput.setClickable(true);
                return true;

            case R.id.action_save:
                final String s = e_categoryinput.getText().toString();

                Boolean doublevalue = false;
                for (Like l : likes) {
                    if (l.getcategoryName().equals(s)) {
                        doublevalue = true;
                        e_categoryinput.setError("category already exists");
                        break;
                    }
                }
                if (!doublevalue && s.length() > 0) {
                    e_categoryinput.setClickable(false);
                    ServerRequestHandler.uploadCategory(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            refreshlikes();
                            e_categoryinput.setText("");
                            e_categoryinput.setVisibility(View.INVISIBLE);
                            refreshlikes();
                            hideKeyboard();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }, s, mContext);
                }
                m_save.setVisible(false);
                m_add.setVisible(true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        g_likes = (GridView) v.findViewById(R.id.g_category_likes);
        e_categoryinput = (EditText) v.findViewById(R.id.e_category_categoryInput);
        refreshlikes();


        g_likes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
                                           int position, long arg3) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Delete");
                alert.setMessage("Do you want to delete this category ?");
                // Set an EditText view to get user input
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TextView txtview = (TextView) arg1.findViewById(R.id.t_likesrow_name);
                        String text = txtview.getText().toString();
                        deleteCat(text);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
                return true;
            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCategoryInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshlikes() {

        ServerRequestHandler.getAllCategories(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                likes.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject o = jsonArray.getJSONObject(i);
                        int id = o.getInt("id");
                        String like = o.getString("name");
                        Like l = new Like(id,like);
                        likes.add(l);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (g_likes != null) {
                    g_likes.setAdapter(new AllCategoriesAdapter(getActivity(), likes));
                    g_likes.invalidate();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse != null)
                    Log.e("NETWORKERROR", volleyError.networkResponse.statusCode + " " + new String(volleyError.networkResponse.data));
                else {
                    if (volleyError.getMessage() == null)
                        Log.e("NETWORKERROR", "timeout");
                    else
                        Log.e("NETWORKERROR", volleyError.getMessage());
                }
            }
        }, mContext);
    }

    public void deleteCat(String input) {
        ServerRequestHandler.DeleteCategory(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    refreshlikes();
                    Log.e("henk", jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, input, mContext);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onCategoryInteraction(Uri uri);
    }

}
