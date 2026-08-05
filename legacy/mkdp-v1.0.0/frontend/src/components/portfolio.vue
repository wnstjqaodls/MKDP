<style>
@import "../assets/css/bootstrap.min.css" ;
@import "../assets/css/form-validation.css" ;

      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
</style>

<template>
<div class="container">
<div>
  <router-view name="IndexHome"></router-view>
</div>

  <main>
    <div class="py-5 text-center">
      <!-- <img class="d-block mx-auto mb-4" src="../assets/bootstrap-logo.svg" alt="" width="72" height="57"> -->      
      <h2>Asset Class Allocation Overview</h2>
      <p class="lead">This portfolio backtesting tool allows you to construct one or more portfolios based on the selected asset class level allocations in order to analyze and backtest portfolio returns, risk characteristics, drawdowns, and rolling returns. You can compare up to three different portfolios against the selected benchmark, and you can also specify any periodic contribution or withdrawal cashflows and the preferred portfolio rebalancing strategy.
You can also use the portfolio backtesting tool to build and compare portfolios based on specific mutual funds, ETFs, and stocks</p>
    </div>

    <div class="row g-3">
       <div class="col-md-5 col-lg-12 order-md-last">
        <h4 class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-primary">Asset</span>
          <!-- <span class="badge bg-primary rounded-pill">3</span> -->
        </h4>
        <ul class="list-group mb-3">
          <li class="list-group-item d-flex justify-content-between lh-sm">          
            <div  class="col-2">
              <h6 class="my-0">Asset<br>Allocation</h6>              
            </div>

            <div  class="col-2">
              <h6 class="my-0">Asset<br>Class</h6>              
            </div>

            <div  class="col-2">
              <h6 class="my-0">Portfolio<br>#1</h6>              
            </div>
              
            <div  class="col-2">
              <h6 class="my-0">Portfolio<br>#2</h6>              
            </div>  
          
            <div  class="col-2">
              <h6 class="my-0">Portfolio<br>#3</h6>              
            </div>         
          </li>

          <!--  -->
          <li class="list-group-item d-flex justify-content-between lh-sm">         
            <div class="col-2">
              <h6 class="my-0">Asset 1</h6>                
            </div>

           <div class="col-2">            
              <select class="form-select" id="AssetClass" required>
                <option value="">Select asset class..</option>
                <option></option>
              </select>
           </div>

          <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>
         
           <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>
        
           <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>        
          </li>

          <!--  -->
          <li class="list-group-item d-flex justify-content-between lh-sm">         
            <div class="col-2">
              <h6 class="my-0">Asset 2</h6>                
            </div>

           <div class="col-2">            
              <select class="form-select" id="AssetClass" required>
                <option value="">Select asset class..</option>
                <option></option>
              </select>
           </div>

          <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>
         
           <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>
        
           <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>        
          </li>

          <!--  -->
          <li class="list-group-item d-flex justify-content-between lh-sm">         
            <div class="col-2">
              <h6 class="my-0">Asset 3</h6>                
            </div>

           <div class="col-2">            
              <select class="form-select" id="AssetClass" required>
                <option value="">Select asset class..</option>
                <option></option>
              </select>
           </div>

          <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>
         
           <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>
        
           <div class="input-group has-validation col-2">
            <input type="text" class="form-control col-5" id="username" placeholder="" required>
            <span class="input-group-text">%</span>            
           </div>        
          </li>                    

        </ul>

        <hr class="my-4">
        <!-- 버튼 -->
          <button class="w-45 btn btn-primary btn-lg" type="submit">Analyze Portfolios</button>&nbsp;          
          <button class="w-45 btn btn-secondary btn-lg" type="button">Cancel</button>        
      </div>

      <!-- 본문 -->
      <div class="col-md-7 col-lg-12">
        <h4 class="mb-3">Portfolio Model Configuration</h4>
          <crop-code-btn></crop-code-btn>
        <form class="needs-validation" novalidate>
          <div class="row g-3">
            <div class="col-sm-6">
              <label for="PortfolioView " class="form-label">Portfolio View</label>                           
              <select class="form-select" id="country" required>
                <option value="">List View</option>
                <option></option>
              </select>
            </div>

            <div class="col-sm-6">
              <label for="PortfolioView " class="form-label">Time Period</label>                           
              <select class="form-select" id="country" required>
                <option value="">Year-to-Year</option>
                <option></option>
              </select>
            </div>

             <div class="col-sm-4">
              <label for="StartYear" class="form-label">Start Year</label>                           
              <select class="form-select" id="country" required>
                <option value="">9999</option>
                <option></option>
              </select>
            </div>

            <div class="col-sm-4">
              <label for="EndYear" class="form-label">End Year</label>                           
              <select class="form-select" id="country" required>
                <option value="">9999</option>
                <option></option>
              </select>
            </div>
            
            <div class="col-sm-4">
              <label for="IncludeYTD" class="form-label">Include YTD</label>                           
              <select class="form-select" id="country" required>
                <option value="">No</option>
                <option></option>
              </select>
            </div>

            <div class="col-6">
              <label for="InitialAmount" class="form-label">Initial Amount</label>
              <div class="input-group has-validation">
                <span class="input-group-text">$</span>
                <input type="text" class="form-control" id="username" placeholder="1000" required>
                <span class="input-group-text">.00</span>
              </div>
            </div>

            <div class="col-6">
              <label for="Cashflows" class="form-label">Cashflows</label>                                       
              <select class="form-select" id="cashflows" required>
                <option value="">None</option>
                <option></option>
              </select>
            </div>

            <div class="col-6">
              <label for="Rebalancing" class="form-label">Rebalancing</label>                                       
              <select class="form-select" id="Rebalancing" required>
                <option value="">Rebalance annually</option>
                <option></option>
              </select>
            </div>

            <div class="col-6">
              <label for="LeverageType" class="form-label">Leverage Type</label>                                       
              <select class="form-select" id="LeverageType" required>
                <option value="">None</option>
                <option></option>
              </select>
            </div>

            <div class="col-6">
              <label for="Benchmark" class="form-label">Benchmark</label>                                       
              <select class="form-select" id="Benchmark" required>
                <option value="">None</option>
                <option></option>
              </select>
            </div>

            <div class="col-6">
              <label for="PortfolioNames" class="form-label">Portfolio Names</label>                                       
              <select class="form-select" id="PortfolioNames" required>
                <option value="">Default</option>
                <option></option>
              </select>
            </div>

          </div>
          <hr class="my-4">
        </form>
      </div>
    </div>

    
  </main>
</div>


</template>

<script>
import axios from 'axios';
import CropCodeBtn from "@/common/CropCodeBtn.vue";
 
export default {
  name: 'MemberInfo',
  components: {CropCodeBtn},
  data() {
    return {
      memberList: [],
    };
  },
  props: {

  },
  created() {
    this.getMemberInfo();
  },
  methods: {
    getMemberInfo() {
      axios
        .get('http://localhost:8050/mkdp-v1.0.0/insertPortfolio')
        .then((response) => {
          if (response.data.success) {
            console.log(response.data.result);
            this.memberList = response.data.result;
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    },
  },
};
</script>
