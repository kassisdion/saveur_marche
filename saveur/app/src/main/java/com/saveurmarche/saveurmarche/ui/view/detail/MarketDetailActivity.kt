package com.saveurmarche.saveurmarche.ui.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.SaveurApplication
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.view.base.BaseActivity
import javax.inject.Inject

class MarketDetailActivity : BaseActivity(), MarketDetailContract.View {

    companion object Builder {
        private const val MARKET_ID_KEY = "MarketDetailActivity.Builder.MARKET_ID_KEY"

        fun newInstance(context: Context, market: Market): Intent {
            return Intent(context, MarketDetailActivity::class.java).apply {
                this.putExtra(MARKET_ID_KEY, market.id)
            }
        }
    }

    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
    */
    private lateinit var mToolbar: Toolbar
    private lateinit var mDescriptionTextView: TextView
    private lateinit var mProductDescriptionTextView : TextView
    private lateinit var mNameTextView: TextView
    private lateinit var mHourTextView: TextView
    private lateinit var mImageViewPicture: ImageView
    private lateinit var mDistanceTextView: TextView
    private lateinit var mUrlTextView: TextView

    /*
    ************************************************************************************************
    ** Injection
    ************************************************************************************************
    */
    @Inject
    lateinit var mPresenter: MarketDetailPresenter

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
     */
    override fun init(savedInstanceState: Bundle?) {
        retrieveWidget()
        setupToolbar()

        setupPresenter()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_market_detail
    }

    /*
    ************************************************************************************************
    ** MarketDetailContract.View implementation
    ************************************************************************************************
     */
    override fun stopProcess() {
        finish()
    }

    override fun seName(displayableName: String) {
        mNameTextView.text = displayableName
        supportActionBar?.title = displayableName
    }

    override fun setHour(hour: String) {
        mHourTextView.text = hour
    }

    override fun setPicture(url: String?) {
        Glide.with(this)
                .setDefaultRequestOptions(RequestOptions()
                        .placeholder(R.drawable.ic_market_colorhint)
                        .error(R.drawable.ic_market_colorhint))
                .load(url)
                .into(mImageViewPicture)
    }

    override fun setDistance(distance: String) {
        mDistanceTextView.text = distance
    }

    override fun setMarketUrl(url: String) {
        mUrlTextView.text = url
    }

    override fun setDescription(displayableDescription: String) {
        mDescriptionTextView.text = displayableDescription
    }

    override fun setProductDescription(displayableProductDescription: String) {
        mProductDescriptionTextView.text = displayableProductDescription
    }


    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
     */
    private fun setupPresenter() {
        SaveurApplication.dataComponent.presenterComponent().inject(this)
        mPresenter.onAttachView(this)

        mPresenter.setArgs(intent.extras.getString(MARKET_ID_KEY))
    }

    private fun retrieveWidget() {
        mToolbar = findViewById(R.id.marketDetailToolbar)
        mDescriptionTextView = findViewById(R.id.marketDetailDescription)
        mProductDescriptionTextView = findViewById(R.id.marketDetailProductDescription)
        mNameTextView = findViewById(R.id.marketDetailTextViewName)
        mHourTextView = findViewById(R.id.marketDetailHour)
        mImageViewPicture = findViewById(R.id.marketDetailImage)
        mDistanceTextView = findViewById(R.id.marketDetailTextViewDistance)
        mUrlTextView = findViewById(R.id.marketDetailUrl)
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)

        supportActionBar?.let {
            with(it) {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                setHomeButtonEnabled(true)
            }
        }
    }
}