import path from "path";
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";

import Unocss from "unocss/vite";
import {
  presetAttributify,
  presetIcons,
  presetUno,
  transformerDirectives,
  transformerVariantGroup,
} from "unocss";

const pathSrc = path.resolve(__dirname, "src");

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      "~/": `${pathSrc}/`,
    },
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "~/styles/element/index.scss" as *;`,
      },
    },
  },
  define: { "process.env": {} },
  plugins: [
    vue(),
    Components({
      // allow auto load markdown components under `./src/components/`
      extensions: ["vue", "md"],
      // allow auto import and register components used in markdown
      include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
      resolvers: [
        ElementPlusResolver({
          importStyle: "sass",
        }),
      ],
      dts: "src/components.d.ts",
    }),

    // https://github.com/antfu/unocss
    // see unocss.config.ts for config
    Unocss({
      presets: [
        presetUno(),
        presetAttributify(),
        presetIcons({
          scale: 1.2,
          warn: true,
        }),
      ],
      transformers: [transformerDirectives(), transformerVariantGroup()],
    }),
  ],
  server: {
    host: "0.0.0.0",
    cors: true,

    port: 3000,
    proxy: {
      "/api": {
        //target: "http://localhost:9090",
        target: "http://127.0.0.1:9090",
      },
      "/auth": {
        //target: "http://localhost:9090",
        target: "http://127.0.0.1:9090",
      },
      "/avatar": {
        target: "https://smms.app/api/v2/upload",
        //target: "https://www.freeimg.cn/api/v1/upload",
        rewrite: (path) => path.replace(/^\/avatar/, ""),
        changeOrigin: true, // 允许跨域

        secure: false,

        //重写url



      },

    },
  },
});
