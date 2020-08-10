<template>
  <footer class="footer px-4">
    <div class="row align-items-center justify-content-lg-between">
      <div class="col-lg-6">
        <div class="copyright text-center text-lg-left text-muted">
          © {{year}} <a href="https://www.creative-tim.com" class="font-weight-bold ml-1" target="_blank">Creative Tim</a> &
          <a href="https://www.binarcode.com" class="font-weight-bold ml-1" target="_blank">Binar Code</a> &
          <a href="#" class="font-weight-bold ml-1" target="_blank">1로와</a>
        </div>
      </div>
      <div class="col-lg-6">
        <ul class="nav nav-footer justify-content-center justify-content-lg-end">
          <li class="nav-item">
            <a href="https://lab.ssafy.com/s03-webmobile1-sub3/s03p13a509/wikis/home" class="nav-link" target="_blank">About Us</a>
          </li>
        </ul>
      </div>
    </div>
  </footer>
</template>
<script>
export default {
  data() {
    return {
      year: new Date().getFullYear()
    };
  }
};
</script>
<style></style>
